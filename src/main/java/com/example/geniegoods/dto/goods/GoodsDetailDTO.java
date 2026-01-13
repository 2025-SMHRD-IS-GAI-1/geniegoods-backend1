package com.example.geniegoods.dto.goods;

import com.example.geniegoods.entity.GoodsEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class GoodsDetailDTO {
    private Long goodsId;
    private String categoryKoreanName;
    private int viewCount;
    private String creatorNickname;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;
    
    private String goodsStyle;
    private String goodsTone;
    private String goodsMood;
    private String prompt;
    private String goodsUrl;

    public static GoodsDetailDTO of(GoodsEntity goods) {
        return GoodsDetailDTO.builder()
                .goodsId(goods.getGoodsId())
                .categoryKoreanName(goods.getGoodsCategoryEntity().getKoreanName())
                .viewCount(goods.getGoodsViewList() != null ? goods.getGoodsViewList().size() : 0)
                .creatorNickname(goods.getUser().getNickname())
                .createdAt(goods.getCreatedAt())
                .goodsStyle(goods.getGoodsStyle() != null ? goods.getGoodsStyle().getKoreanName() : null)
                .goodsTone(goods.getGoodsTone() != null ? goods.getGoodsTone().getKoreanName() : null)
                .goodsMood(goods.getGoodsMood() != null ? goods.getGoodsMood().getKoreanName() : null)
                .prompt(goods.getPrompt())
                .goodsUrl(goods.getGoodsUrl())
                .build();
    }
}