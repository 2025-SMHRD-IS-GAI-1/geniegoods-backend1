package com.example.geniegoods.dto.goods;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Schema(description = "굿즈 이미지 생성 응답")
public class CreateGoodsImgResponseDTO {
    @Schema(description = "메세지", example = "업로드 이미지 Object Storage 저장 실패")
    private String message;
    @Schema(description = "굿즈 이미지 url", example = "https://kr.object.ncloudstorage.com")
    private String goodsImgUrl;
    @Schema(description = "굿즈 이미지 사이즈", example = "1013914")
    private Long goodsImgSize;
    @Schema(description = "업로드한 이미지 그룹 pk", example = "1")
    private Long uploadImgGroupId;
}
