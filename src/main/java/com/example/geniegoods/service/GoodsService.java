package com.example.geniegoods.service;

import com.example.geniegoods.dto.goods.*;
import com.example.geniegoods.entity.*;
import com.example.geniegoods.enums.GoodsMood;
import com.example.geniegoods.enums.GoodsStyle;
import com.example.geniegoods.enums.GoodsTone;
import com.example.geniegoods.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j  // log 사용을 위해 추가 (필요시)
public class GoodsService {

    private final GoodsRepository goodsRepository;
    private final UploadImgRepository uploadImgRepository;
    private final UploadImgGroupRepository uploadImgGroupRepository;
    private final GoodsCategoryRepository goodsCategoryRepository;
    private final S3Client s3Client;  // 그대로 유지 (S3Client는 주입받음)
    private final GoodsViewRepository goodsViewRepository;

    @Value("${app.object-storage.bucket-name}")
    private String bucketName;

    @Value("${app.object-storage.endpoint}")
    private String endpoint;  // URL 추출에 필요

    @Transactional
    public void deleteGoodsByIds(List<Long> goodsIds, Long currentUserId) {
        for (Long goodsId : goodsIds) {
            GoodsEntity goods = goodsRepository.findById(goodsId)
                    .orElseThrow(() -> new IllegalArgumentException("굿즈를 찾을 수 없습니다: " + goodsId));

            // 보안 체크
            if (!goods.getUser().getUserId().equals(currentUserId)) {
                throw new IllegalStateException("본인의 굿즈만 삭제할 수 있습니다.");
            }

            UploadImgGroupEntity imgGroup = goods.getUploadImgGroup();
            if (imgGroup != null) {
                List<UploadImgEntity> imgList = uploadImgRepository.findByUploadImgGroup(imgGroup);

                // 1. 실제 이미지 파일 삭제
                for (UploadImgEntity img : imgList) {
                    String imgUrl = img.getUploadImgUrl();
                    if (imgUrl != null && !imgUrl.isEmpty()) {
                        deleteImageFromStorage(imgUrl);
                    }
                }

                // 2. 자식 엔티티 삭제 (UploadImgEntity)
                uploadImgRepository.deleteAll(imgList);

                // 3. 부모 먼저 삭제 (GoodsEntity) ← 이게 핵심!
                goodsRepository.delete(goods);

                // 4. 자식 그룹 마지막에 삭제
                uploadImgGroupRepository.delete(imgGroup);
            } else {
                // 그룹 없으면 Goods만 삭제
                goodsRepository.delete(goods);
            }
        }
    }

    /**
     * Object Storage에서 이미지 삭제 (공통 메서드)
     */
    private void deleteImageFromStorage(String imageUrl) {
        if (imageUrl == null || imageUrl.isEmpty()) {
            return;
        }

        try {
            // URL에서 key 추출 (ObjectStorageService의 deleteProfileImage와 동일 로직)
            int bucketIndex = imageUrl.indexOf(bucketName);
            if (bucketIndex == -1) {
                log.warn("버킷 이름이 URL에 없습니다: {}", imageUrl);
                return;
            }

            String key = imageUrl.substring(bucketIndex + bucketName.length() + 1);
            if (key.isEmpty()) {
                log.warn("추출된 키가 비어있습니다: {}", imageUrl);
                return;
            }

            DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            s3Client.deleteObject(deleteRequest);
            log.info("Object Storage에서 이미지 삭제 성공: {}", key);

        } catch (Exception e) {
            log.warn("Object Storage 이미지 삭제 실패 (URL: {}): {}", imageUrl, e.getMessage());
            // 실패해도 전체 트랜잭션 중단하지 않음 (이미지 없거나 이미 삭제된 경우 많음)
        }
    }
    
    @Transactional
    public SelectGoodsResponseDTO selectGoods(UserEntity user, SelectGoodsRequestDTO dto, String goodsImgUrl, MultipartFile goodsImgFile) {
        SelectGoodsResponseDTO response = new SelectGoodsResponseDTO();

        // 카테고리 조회
        GoodsCategoryEntity category = goodsCategoryRepository.findByKoreanName(dto.getCategory())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리입니다: " + dto.getCategory()));

        // 업로드 그룹 조회
        UploadImgGroupEntity imgGroup = uploadImgGroupRepository.findById(dto.getUploadImgGroupId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 업로드 그룹입니다: " + dto.getUploadImgGroupId()));

        // Enum 변환 (null 허용, 빈 문자열도 null로 처리)
        GoodsStyle style = (dto.getGoodsStyle() != null && !dto.getGoodsStyle().isEmpty()) 
                ? GoodsStyle.fromKoreanName(dto.getGoodsStyle()) : null;
        GoodsTone tone = (dto.getGoodsTone() != null && !dto.getGoodsTone().isEmpty()) 
                ? GoodsTone.fromKoreanName(dto.getGoodsTone()) : null;
        GoodsMood mood = (dto.getGoodsMood() != null && !dto.getGoodsMood().isEmpty()) 
                ? GoodsMood.fromKoreanName(dto.getGoodsMood()) : null;

        // GoodsEntity 생성 및 저장
        GoodsEntity goods = GoodsEntity.builder()
                .goodsUrl(goodsImgUrl)
                .goodsStyle(style)
                .goodsTone(tone)
                .goodsMood(mood)
                .goodsImgSize(goodsImgFile.getSize())
                .prompt(dto.getPrompt())
                .user(user)
                .uploadImgGroup(imgGroup)
                .goodsCategoryEntity(category)
                .build();

        goodsRepository.save(goods);

        response.setStatus("SUCCESS");
        response.setMessage("굿즈 선택 완료");
        return response;
    }

    /**
     * 뒤로가기시 Object Storage smaple 이미지 삭제
     * @param request
     * @return
     */
    public DeleteGoodsSampleResponseDTO deleteSampleImg(DeleteGoodsSampleRequestDTO request) {
        DeleteGoodsSampleResponseDTO response = new DeleteGoodsSampleResponseDTO();

        // url이 안넘어 올때
        if(request.getGoodsSampleImgUrl().isEmpty()) {
            log.warn("이미지 URL이 비어있습니다.");
            response.setStatus("ERROR");
            response.setMessage("이미지 URL이 비어있습니다.");
            return response;
        }

        // 이미지 삭제
        for (String url : request.getGoodsSampleImgUrl()) {
            deleteImageFromStorage(url);
        }

        response.setStatus("SUCCESS");
        response.setMessage("이미지 삭제 완료");
        return response;
    }

    /**
     * 내가 생성한 굿즈 리스트 불러오기
     * @param user
     * @return
     */
    public List<SelectAllMyGoodsResponseDTO> selectAllMyGoods(UserEntity user) {
        List<GoodsEntity> goodsEntityList = goodsRepository.findByUser(user);

        List<SelectAllMyGoodsResponseDTO> response = new ArrayList<>();

        for(GoodsEntity goodsEntity : goodsEntityList) {
            response.add(SelectAllMyGoodsResponseDTO.of(goodsEntity));
        }

        return response;
    }
    
    public List<GoodsBrowseDTO> browseGoods(Long categoryId, Pageable pageable) {
        Page<GoodsBrowseProjection> page = goodsRepository.findByCategoryWithViews(categoryId, pageable);
        return page.getContent().stream().map(proj -> GoodsBrowseDTO.builder()
                .goodsId(proj.getGoods().getGoodsId())
                .goodsUrl(proj.getGoods().getGoodsUrl())
                .categoryKoreanName(proj.getGoods().getGoodsCategoryEntity().getKoreanName())
                .viewCount(proj.getViewCount())
                .creatorNickname(proj.getGoods().getUser().getNickname())
                .createdAt(proj.getGoods().getCreatedAt())
                .goodsStyle(proj.getGoods().getGoodsStyle() != null ? proj.getGoods().getGoodsStyle().getKoreanName() : null)
                .goodsTone(proj.getGoods().getGoodsTone() != null ? proj.getGoods().getGoodsTone().getKoreanName() : null)
                .goodsMood(proj.getGoods().getGoodsMood() != null ? proj.getGoods().getGoodsMood().getKoreanName() : null)
                .prompt(proj.getGoods().getPrompt())
                .build()).collect(Collectors.toList());
    }
    
    /**
     * 굿즈 상세 조회 (조회수 1증가)
     * @param goodsId
     * @param user
     */
    public void viewGoods(Long goodsId, UserEntity user) {
        GoodsEntity goods = goodsRepository.findById(goodsId)
                .orElseThrow(() -> new IllegalArgumentException("Goods not found"));

        if (!goods.getIsPublic()) {
            throw new IllegalArgumentException("Private goods");
        }

        // 회원이 같은 굿즈를 볼때 한번만 조회
        if (user != null) {
            GoodsViewEntity view = goodsViewRepository.findByGoodsGoodsIdAndUserUserId(goodsId, user.getUserId());
            if (view == null) {
                GoodsViewEntity newView = GoodsViewEntity.builder()
                    .goods(goods)
                    .user(user)
                    .viewedAt(LocalDateTime.now())
                    .build();
                goodsViewRepository.save(newView);
            }
        }
    }
}