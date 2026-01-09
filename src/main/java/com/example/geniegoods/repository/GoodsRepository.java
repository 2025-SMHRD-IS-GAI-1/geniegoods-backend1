package com.example.geniegoods.repository;

import com.example.geniegoods.entity.GoodsEntity;
import com.example.geniegoods.entity.UserEntity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface GoodsRepository extends JpaRepository<GoodsEntity, Long> {

    List<GoodsEntity> findByUser(UserEntity user);

    // 탈퇴 시 사용자의 모든 굿즈를 비공개/공개 처리
    @Modifying
    @Transactional
    @Query("UPDATE GoodsEntity g SET g.isPublic = :isPublic WHERE g.user.userId = :userId")
    void updateIsPublicByUserId(@Param("userId") Long userId, @Param("isPublic") boolean isPublic);

    // 필요시 추가: 사용자별 굿즈 목록 조회
    // List<GoodsEntity> findByUserUserId(Long userId);
    @Query("SELECT g as goods, COUNT(v) as viewCount " +
    	       "FROM GoodsEntity g " +
    	       "LEFT JOIN GoodsViewEntity v ON v.goods = g " +
    	       "WHERE (:categoryId IS NULL OR g.goodsCategoryEntity.categoryId = :categoryId) " +
    	       "GROUP BY g " +
    	       "ORDER BY viewCount DESC")
    	Page<GoodsBrowseProjection> findByCategoryWithViews(@Param("categoryId") Long categoryId, Pageable pageable);
}
