package com.example.geniegoods.dto.goods;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "굿즈 시안 선택 응답")
public class SelectGoodsResponseDTO {
    @Schema(description = "메세지", example = "굿즈 선택 완료")
    private String message;
}
