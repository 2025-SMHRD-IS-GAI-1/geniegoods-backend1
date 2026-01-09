package com.example.geniegoods.dto.goods;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class SelectGoodsRequestDTO {
    private String resultImageUrl;
    private List<String> sampleGoodsImageUrl;
    private String goodsImgUrl;
    private String prompt;
    private String category;
    private Long uploadImgGroupId;
    private String goodsStyle;
    private String goodsTone;
    private String goodsMood;
}
