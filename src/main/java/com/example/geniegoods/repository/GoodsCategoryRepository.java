package com.example.geniegoods.repository;

import com.example.geniegoods.entity.GoodsCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GoodsCategoryRepository extends JpaRepository<GoodsCategoryEntity, Long> {
    Optional<GoodsCategoryEntity> findByKoreanName(String koreanName);
}
