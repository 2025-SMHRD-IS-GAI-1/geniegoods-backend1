package com.example.geniegoods.dto.goods;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Schema(description = "오늘 생성한 굿즈 개수 응답")
public class GoodsTodayCountResponseDTO {
    @Schema(description = "오늘 생성한 굿즈 개수", example = "5")
    private int todayCount;
}
