package com.example.geniegoods.repository;

import com.example.geniegoods.entity.GoodsEntity;

public interface GoodsBrowseProjection {
    GoodsEntity getGoods();
    Long getViewCount();
}