package com.example.geniegoods.service;

import com.example.geniegoods.dto.goods.CreateGoodsImgRequestDTO;
import com.example.geniegoods.dto.goods.NanobananaComposeResponseDTO;
import com.example.geniegoods.dto.goods.NanoBananaSampleResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NanoService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    private static final String NANO_COMPOSE_API_URL = "http://localhost:8001/api/nano/compose";
    private static final String NANO_API_URL = "http://localhost:8001/api/nano/sample";

    /**
     * nano api 호출 - 객체 이미지와 옵션을 받아서 굿즈 이미지 생성
     * @param objectImgFiles YOLO에서 생성된 객체 이미지 바이트 배열 리스트
     * @param dto 굿즈 생성 옵션 (style, color, mood, category, description)
     * @return FastAPI에서 생성된 굿즈 이미지 파일
     */
    public MultipartFile createGoodsImage(List<byte[]> objectImgFiles, CreateGoodsImgRequestDTO dto) {
        try {
            if (objectImgFiles == null || objectImgFiles.isEmpty()) {
                throw new IllegalArgumentException("객체 이미지 파일이 없습니다.");
            }

            // 1. 프롬프트 생성 (DTO의 옵션들을 조합)
            String prompt = dto.getDescription();
            log.info("생성된 프롬프트: {}", prompt);

            // 2. byte 배열과 프롬프트를 FastAPI가 받을 수 있는 형태로 변환
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            
            // 객체 이미지 파일들 추가
            for (byte[] imageBytes : objectImgFiles) {
                if (imageBytes != null && imageBytes.length > 0) {
                    Resource resource = new ByteArrayResource(imageBytes) {
                        @Override
                        public String getFilename() {
                            return "object_image.jpg";
                        }
                    };
                    body.add("files", resource);
                }
            }

            // 프롬프트 추가
            body.add("prompt", prompt);

            // 3. HTTP 헤더 설정
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            // 4. HTTP 요청 엔티티 생성
            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            // 5. FastAPI 호출 - JSON 응답 받기
            log.info("Nano API 호출 시작: {}", NANO_COMPOSE_API_URL);
            log.info("전송 옵션 - style: {}, color: {}, mood: {}, category: {}, description: {}", 
                    dto.getStyle(), dto.getColor(), dto.getMood(), dto.getCategory(), dto.getDescription());
            
            ResponseEntity<String> response = restTemplate.exchange(
                    NANO_COMPOSE_API_URL,
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );

            // 6. JSON 응답 파싱
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                NanobananaComposeResponseDTO composeResponse = objectMapper.readValue(
                        response.getBody(),
                        NanobananaComposeResponseDTO.class
                );

                if (composeResponse.getSaved() == null || !composeResponse.getSaved()) {
                    throw new RuntimeException("이미지 합성에 실패했습니다. 프롬프트를 확인해주세요.");
                }

                // 7. base64 디코딩하여 MultipartFile로 변환
                String resultData = composeResponse.getResult_data();
                if (resultData == null || resultData.isEmpty()) {
                    throw new RuntimeException("결과 이미지 데이터가 없습니다.");
                }
                
                byte[] imageBytes;
                try {
                    imageBytes = Base64.getDecoder().decode(resultData);
                } catch (IllegalArgumentException e) {
                    log.error("결과 이미지 base64 디코딩 실패", e);
                    throw new RuntimeException("결과 이미지 디코딩 실패: " + e.getMessage(), e);
                }
                
                MultipartFile goodsImage = new MultipartFile() {
                    @Override
                    public String getName() {
                        return "goods_image";
                    }

                    @Override
                    public String getOriginalFilename() {
                        return "goods_image.png";
                    }

                    @Override
                    public String getContentType() {
                        return "image/png";
                    }

                    @Override
                    public boolean isEmpty() {
                        return imageBytes == null || imageBytes.length == 0;
                    }

                    @Override
                    public long getSize() {
                        return imageBytes != null ? imageBytes.length : 0;
                    }

                    @Override
                    public byte[] getBytes() throws IOException {
                        return imageBytes;
                    }

                    @Override
                    public java.io.InputStream getInputStream() throws IOException {
                        return new java.io.ByteArrayInputStream(imageBytes);
                    }

                    @Override
                    public void transferTo(java.io.File dest) throws IOException, IllegalStateException {
                        throw new UnsupportedOperationException("Not implemented");
                    }
                };
                
                log.info("Nano API 호출 성공: 이미지 크기 {} bytes", imageBytes.length);
                return goodsImage;
            } else {
                log.error("Nano API 호출 실패: 상태 코드 {}", response.getStatusCode());
                throw new RuntimeException("Nano API 호출 실패: " + response.getStatusCode());
            }

        } catch (Exception e) {
            log.error("Nano API 호출 중 오류 발생", e);
            throw new RuntimeException("Nano API 호출 중 오류: " + e.getMessage(), e);
        }
    }

    /**
     * nano api 호출 - 이미지 파일 바이트 배열을 받아서 굿즈 시안 이미지 3개 생성
     * @param resultGoodsImageFileByte 굿즈 이미지 파일 바이트 배열
     * @return 굿즈 시안 이미지 3개 파일
     */
    public List<MultipartFile> createGoodsSampleImage(byte[] resultGoodsImageFileByte) {
        try {
            // 1. MultipartFile과 옵션을 FastAPI가 받을 수 있는 형태로 변환
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            
            Resource resource = new ByteArrayResource(resultGoodsImageFileByte) {
                @Override
                public String getFilename() {
                    return "result-image.jpg";
                }
            };
            body.add("result_image", resource);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            ResponseEntity<byte[]> response = restTemplate.exchange(
                NANO_API_URL,
                HttpMethod.POST,
                requestEntity,
                byte[].class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {

                // JSON 파싱
                NanoBananaSampleResponseDTO sampleResponse = objectMapper.readValue(
                    response.getBody(),
                    NanoBananaSampleResponseDTO.class
                );

                List<MultipartFile> sampleGoodsImages = new ArrayList<>();

                // reuslt_data_list의 각 base64 문자열을 디코딩
                if(sampleResponse.getResult_data_list() != null) {
                    for (int i = 0; i < sampleResponse.getResult_data_list().size(); i++) {
                        String base64Data = sampleResponse.getResult_data_list().get(i);
                        byte[] imageBytes = Base64.getDecoder().decode(base64Data);

                        int finalI = i;

                        MultipartFile sampleGoodsImage = new MultipartFile() {
                            @Override
                            public String getName() {
                                return "sample-image-" + finalI;
                            }
                            @Override
                            public String getOriginalFilename() {
                                return "sample-image-" + finalI + ".jpg";
                            }
                            @Override
                            public String getContentType() {
                                return "image/jpeg";
                            }
                            @Override
                            public boolean isEmpty() {
                                return imageBytes == null || imageBytes.length == 0;
                            }
                            @Override
                            public long getSize() {
                                return imageBytes != null ? imageBytes.length : 0;
                            }
                            @Override
                            public byte[] getBytes() throws IOException {
                                return imageBytes;
                            }
                            @Override
                            public java.io.InputStream getInputStream() throws IOException {
                                return new java.io.ByteArrayInputStream(imageBytes);
                            }
                            @Override
                            public void transferTo(java.io.File dest) throws IOException, IllegalStateException {
                                throw new UnsupportedOperationException("Not implemented");
                            }
                        };
                        sampleGoodsImages.add(sampleGoodsImage);
                    }
                }

                return sampleGoodsImages;
            } else {
                log.error("Nano API 호출 실패: 상태 코드 {}", response.getStatusCode());
                throw new RuntimeException("Nano API 호출 실패: " + response.getStatusCode());
            }
        } catch (Exception e) {
            log.error("Nano API 호출 중 오류 발생", e);
            throw new RuntimeException("Nano API 호출 중 오류: " + e.getMessage(), e);
        }
    }
}