package com.example.geniegoods.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;

import java.io.IOException;
import java.util.UUID;

/**
 * Naver Cloud Object Storage 서비스
 */
@Service
@RequiredArgsConstructor
public class ObjectStorageService {

    private final S3Client s3Client;

    @Value("${app.object-storage.bucket-name}")
    private String bucketName;

    @Value("${app.object-storage.endpoint}")
    private String endpoint;

    /**
     * 프로필 이미지를 Object Storage에 업로드
     * @param file 업로드할 파일
     * @param userId 사용자 ID (파일명에 포함)
     * @return 업로드된 파일의 URL
     */
    public String uploadFile(MultipartFile file, Long userId, String folderName) throws IOException {

        // 파일 확장자 추출
        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        // 고유한 파일명 생성: profile/{userId}/{uuid}.{extension}
        String fileName = folderName +  "/" + UUID.randomUUID() + extension;

        // Object Storage에 업로드
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .contentType(file.getContentType())
                .acl(ObjectCannedACL.PUBLIC_READ)
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

        // 업로드된 파일의 URL 반환
        return endpoint + "/" + bucketName + "/" + fileName;

    }

    /**
     * 기존 이미지 삭제
     * @param imageUrl 삭제할 이미지의 URL
     */
    public void deleteImage(String imageUrl) {
        if (imageUrl == null || imageUrl.isEmpty()) {
            return;
        }

        try {
            // URL에서 파일 키 추출
            // 예: https://kr.object.ncloudstorage.com/bucket-name/profile/1/uuid.jpg
            int bucketIndex = imageUrl.indexOf(bucketName);
            if (bucketIndex == -1) {
                // bucketName이 URL에 없으면 전체 URL을 키로 사용하지 않음
                return;
            }
            
            String key = imageUrl.substring(bucketIndex + bucketName.length() + 1);
            
            // key가 비어있으면 삭제하지 않음
            if (key.isEmpty()) {
                return;
            }

            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            s3Client.deleteObject(deleteObjectRequest);
        } catch (Exception e) {
            // 삭제 실패 시 로그만 남기고 예외는 던지지 않음 (기존 이미지가 없을 수도 있음)
            System.err.println("이미지 삭제 실패: " + e.getMessage());
        }
    }

    /**
     * Object Storage에서 이미지 다운로드
     * @param imageUrl 다운로드할 이미지의 URL
     * @return 이미지 바이트 배열
     */
    public byte[] downloadImage(String imageUrl) throws IOException {
        if (imageUrl == null || imageUrl.isEmpty()) {
            throw new IllegalArgumentException("이미지 URL이 비어있습니다.");
        }

        // URL에서 파일 키 추출
        // 예: https://kr.object.ncloudstorage.com/bucket-name/sample/1/uuid.jpg
        int bucketIndex = imageUrl.indexOf(bucketName);
        if (bucketIndex == -1) {
            throw new IllegalArgumentException("유효하지 않은 이미지 URL입니다.");
        }
        
        String key = imageUrl.substring(bucketIndex + bucketName.length() + 1);
        
        if (key.isEmpty()) {
            throw new IllegalArgumentException("이미지 키를 추출할 수 없습니다.");
        }

        // S3에서 객체 가져오기
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();
            
            ResponseInputStream<GetObjectResponse> response = s3Client.getObject(getObjectRequest);
            
            return response.readAllBytes();
        } catch (Exception e) {
            throw new IOException("이미지 다운로드 실패: " + e.getMessage(), e);
        }
    }

}

