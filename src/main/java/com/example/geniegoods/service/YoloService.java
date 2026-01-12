package com.example.geniegoods.service;

import com.example.geniegoods.dto.goods.YoloDetectionResponseDTO;
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

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class YoloService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    private static final String YOLO_API_URL = "http://localhost:8000/api/yolo/detect";

    /**
     * yolo api 호출 업로드 이미지를 객체만 딴 이미지로 변환
     *
     * @param uploadImgFiles 업로드된 이미지 파일 배열
     * @return FastAPI에서 생성된 객체 이미지 파일 리스트
     */
    public List<byte[]> createObjectDetetctionImage(MultipartFile[] uploadImgFiles) {

        List<byte[]> resultBytesList = new ArrayList<>();

        try {
            if (uploadImgFiles == null || uploadImgFiles.length == 0) {
                throw new IllegalArgumentException("업로드할 이미지 파일이 없습니다.");
            }

            // 1. MultipartFile을 FastAPI가 받을 수 있는 형태로 변환
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

            // 각 파일을 Resource로 변환하여 추가
            for (MultipartFile file : uploadImgFiles) {
                if (file != null && !file.isEmpty()) {
                    Resource resource = new ByteArrayResource(file.getBytes()) {
                        @Override
                        public String getFilename() {
                            return file.getOriginalFilename();
                        }
                    };
                    body.add("files", resource);
                }
            }

            // 2. HTTP 헤더 설정
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            // 3. HTTP 요청 엔티티 생성
            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            // 4. FastAPI 호출 - JSON 응답 받기
            log.info("YOLO API 호출 시작: {}", YOLO_API_URL);
            ResponseEntity<String> response = restTemplate.exchange(
                    YOLO_API_URL,
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );

            // 5. JSON 응답 파싱
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                YoloDetectionResponseDTO detectionResponse = objectMapper.readValue(
                        response.getBody(),
                        YoloDetectionResponseDTO.class
                );

                log.info("YOLO API 응답: 총 탐지={}, 저장된 탐지={}, 크롭 개수={}",
                        detectionResponse.getTotal_detections(),
                        detectionResponse.getKept_detections(),
                        detectionResponse.getCrops() != null ? detectionResponse.getCrops().size() : 0);
                // 6. base64 디코딩하여 byte[] 리스트로 반환

                if (detectionResponse.getCrops() != null) {
                    for (YoloDetectionResponseDTO.CropInfoDTO crop : detectionResponse.getCrops()) {
                        String cropData = crop.getCrop_data();
                        if (cropData == null || cropData.isEmpty()) {
                            continue;
                        }

                        byte[] imageBytes = Base64.getDecoder().decode(cropData);
                        resultBytesList.add(imageBytes);
                    }
                }

                return resultBytesList;
            }
        } catch (Exception e) {
            log.error("YOLO API 호출 중 오류 발생", e);
            throw new RuntimeException("YOLO API 호출 중 오류: " + e.getMessage(), e);
        }
        return resultBytesList;
    }
}