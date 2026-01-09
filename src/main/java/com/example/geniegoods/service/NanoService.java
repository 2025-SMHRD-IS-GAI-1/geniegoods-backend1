package com.example.geniegoods.service;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.example.geniegoods.dto.goods.CreateGoodsImgRequestDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class NanoService {

    private final RestTemplate restTemplate;

    private static final String NANO_API_URL = "http://localhost:8000/api/nano";

    /**
     * nano api 호출 - 객체 이미지와 옵션을 받아서 굿즈 이미지 생성
     * @param objectImgFiles YOLO에서 생성된 객체 이미지 파일 배열
     * @param dto 굿즈 생성 옵션 (style, color, mood, category, description)
     * @return FastAPI에서 생성된 굿즈 이미지 파일
     */
    public MultipartFile createGoodsImage(MultipartFile[] objectImgFiles, CreateGoodsImgRequestDTO dto) {
        try {
            // 1. MultipartFile과 옵션을 FastAPI가 받을 수 있는 형태로 변환
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            
            // 객체 이미지 파일들 추가
            for (int i = 0; i < objectImgFiles.length; i++) {
                MultipartFile file = objectImgFiles[i];
                if (file != null && !file.isEmpty()) {
                    Resource resource = new ByteArrayResource(file.getBytes()) {
                        @Override
                        public String getFilename() {
                            return file.getOriginalFilename();
                        }
                    };
                    body.add("object_images", resource); // FastAPI에서 받는 파라미터명에 맞게 수정
                }
            }

            // 옵션 정보 추가 (텍스트 파라미터)
            if (dto.getStyle() != null && !dto.getStyle().isEmpty()) {
                body.add("style", dto.getStyle());
            }
            if (dto.getColor() != null && !dto.getColor().isEmpty()) {
                body.add("color", dto.getColor());
            }
            if (dto.getMood() != null && !dto.getMood().isEmpty()) {
                body.add("mood", dto.getMood());
            }
            if (dto.getCategory() != null && !dto.getCategory().isEmpty()) {
                body.add("category", dto.getCategory());
            }
            if (dto.getDescription() != null && !dto.getDescription().isEmpty()) {
                body.add("description", dto.getDescription());
            }

            // 2. HTTP 헤더 설정
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            // 3. HTTP 요청 엔티티 생성
            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            // 4. FastAPI 호출
            log.info("Nano API 호출 시작: {}", NANO_API_URL);
            log.info("전송 옵션 - style: {}, color: {}, mood: {}, category: {}", 
                    dto.getStyle(), dto.getColor(), dto.getMood(), dto.getCategory());
            
            ResponseEntity<byte[]> response = restTemplate.exchange(
                    NANO_API_URL,
                    HttpMethod.POST,
                    requestEntity,
                    byte[].class
            );

            // 5. 응답을 MultipartFile로 변환
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                byte[] imageBytes = response.getBody();
                
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
    public MultipartFile[] createGoodsSampleImage(byte[] resultGoodsImageFileByte) {
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
                byte[] imageBytes = response.getBody();
                MultipartFile[] sampleGoodsImages = new MultipartFile[3];
                for (int i = 0; i < 3; i++) {
                    int finalI = i;
                    sampleGoodsImages[i] = new MultipartFile() {
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