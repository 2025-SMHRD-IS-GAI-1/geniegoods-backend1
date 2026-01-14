package com.example.geniegoods.dto.goods;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Schema(description = "굿즈 시안 선택 요청")
public class CreateGoodsSampleRequestDTO {
    @Schema(description = "상태 값", example = "SUCCESS")
    private String resultImageUrl; // Object Storage URL (우선 사용)
    @Schema(description = "상태 값", example = "SUCCESS")
    private String description;
    @Schema(description = "상태 값", example = "SUCCESS")
    private String category;
    @Schema(description = "상태 값", example = "SUCCESS")
    private String style;
    @Schema(description = "상태 값", example = "SUCCESS")
    private String color;
    @Schema(description = "상태 값", example = "SUCCESS")
    private String mood;
    @Schema(description = "상태 값", example = "SUCCESS")
    private Long uploadImgGroupId;
}
