package com.example.geniegoods.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import java.net.URI;

/**
 * Naver Cloud Object Storage 설정
 * Naver Cloud Object Storage는 AWS S3 호환 API를 사용합니다.
 */
@Configuration
public class ObjectStorageConfig {

    @Value("${app.object-storage.endpoint}")
    private String endpoint;

    @Value("${app.object-storage.region}")
    private String region;

    @Value("${app.object-storage.access-key}")
    private String accessKey;

    @Value("${app.object-storage.secret-key}")
    private String secretKey;

    @Bean
    public S3Client s3Client() {
        AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);

        return S3Client.builder()
                .endpointOverride(URI.create(endpoint))
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .forcePathStyle(true) // Naver Object Storage는 path-style 사용
                .build();
    }
}

