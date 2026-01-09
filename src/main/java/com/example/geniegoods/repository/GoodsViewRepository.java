package com.example.geniegoods.repository;

import com.example.geniegoods.entity.GoodsViewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GoodsViewRepository extends JpaRepository<GoodsViewEntity, Long> {
    GoodsViewEntity findByGoodsGoodsIdAndUserUserId(Long goodsId, Long userId);
}