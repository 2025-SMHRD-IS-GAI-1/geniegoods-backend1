package com.example.geniegoods.dto.goods;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Schema(description = "굿즈 상세보기 요청")
public class GoodsDetailRequestDTO {
    @Schema(description = "굿즈 PK", example = "1")
    private Long goodsId;
}
