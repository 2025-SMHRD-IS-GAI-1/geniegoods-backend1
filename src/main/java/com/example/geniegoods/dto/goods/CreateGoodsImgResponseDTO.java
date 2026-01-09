package com.example.geniegoods.dto.goods;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CreateGoodsImgResponseDTO {
    private String status;
    private String message;
    private String goodsImgUrl;
    private Long goodsImgSize;
    private Long uploadImgGroupId;
}
