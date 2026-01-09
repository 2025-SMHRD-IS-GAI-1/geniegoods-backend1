package com.example.geniegoods.service;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class YoloService {

    private final RestTemplate restTemplate;

    private static final String YOLO_API_URL = "http://localhost:8000/api/yolo";

    /**
     * yolo api 호출 업로드 이미지를 객체만 딴 이미지로 변환
     * @param uploadImgFiles 업로드된 이미지 파일 배열
     * @return FastAPI에서 생성된 객체 이미지 파일 배열
     */
    public MultipartFile[] createGoodsImage(MultipartFile[] uploadImgFiles) {
        try {
            // 1. MultipartFile을 FastAPI가 받을 수 있는 형태로 변환
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            
            // 각 파일을 Resource로 변환하여 추가
            for (int i = 0; i < uploadImgFiles.length; i++) {
                MultipartFile file = uploadImgFiles[i];
                if (file != null && !file.isEmpty()) {
                    Resource resource = new ByteArrayResource(file.getBytes()) {
                        @Override
                        public String getFilename() {
                            return file.getOriginalFilename();
                        }
                    };
                    body.add("files", resource); // FastAPI에서 받는 파라미터명에 맞게 수정
                }
            }

            // 2. HTTP 헤더 설정
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            // 3. HTTP 요청 엔티티 생성
            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            // 4. FastAPI 호출
            log.info("YOLO API 호출 시작: {}", YOLO_API_URL);
            ResponseEntity<byte[][]> response = restTemplate.exchange(
                    YOLO_API_URL,
                    HttpMethod.POST,
                    requestEntity,
                    byte[][].class
            );

            // 5. 응답을 MultipartFile 배열로 변환
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                byte[][] responseFiles = response.getBody();
                List<MultipartFile> resultFiles = new ArrayList<>();
                
                for (int i = 0; i < responseFiles.length; i++) {
                    byte[] fileBytes = responseFiles[i];
                    final int index = i;
                    
                    MultipartFile multipartFile = new MultipartFile() {
                        @Override
                        public String getName() {
                            return "object_image_" + index;
                        }

                        @Override
                        public String getOriginalFilename() {
                            return "object_image_" + index + ".png";
                        }

                        @Override
                        public String getContentType() {
                            return "image/png";
                        }

                        @Override
                        public boolean isEmpty() {
                            return fileBytes == null || fileBytes.length == 0;
                        }

                        @Override
                        public long getSize() {
                            return fileBytes != null ? fileBytes.length : 0;
                        }

                        @Override
                        public byte[] getBytes() throws IOException {
                            return fileBytes;
                        }

                        @Override
                        public java.io.InputStream getInputStream() throws IOException {
                            return new java.io.ByteArrayInputStream(fileBytes);
                        }

                        @Override
                        public void transferTo(java.io.File dest) throws IOException, IllegalStateException {
                            throw new UnsupportedOperationException("Not implemented");
                        }
                    };
                    
                    resultFiles.add(multipartFile);
                }
                
                log.info("YOLO API 호출 성공: {}개 파일 반환", resultFiles.size());
                return resultFiles.toArray(new MultipartFile[0]);
            } else {
                log.error("YOLO API 호출 실패: 상태 코드 {}", response.getStatusCode());
                throw new RuntimeException("YOLO API 호출 실패: " + response.getStatusCode());
            }

        } catch (Exception e) {
            log.error("YOLO API 호출 중 오류 발생", e);
            throw new RuntimeException("YOLO API 호출 중 오류: " + e.getMessage(), e);
        }
    }
}