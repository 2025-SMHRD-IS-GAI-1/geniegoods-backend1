package com.example.geniegoods.dto.goods;

import com.example.geniegoods.entity.GoodsEntity;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class SelectAllMyGoodsResponseDTO {
    private Long goodsId;
    private String goodsUrl;
    private Integer price;

    public static SelectAllMyGoodsResponseDTO of(GoodsEntity goodsEntity) {
        return SelectAllMyGoodsResponseDTO.builder()
            .goodsId(goodsEntity.getGoodsId())
            .goodsUrl(goodsEntity.getGoodsUrl())
            .price(goodsEntity.getGoodsCategoryEntity().getPrice())
            .build();
    }
}
