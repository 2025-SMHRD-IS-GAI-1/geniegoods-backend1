package com.example.geniegoods.dto.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Schema(description = "공통 응답")
public class CommonResponseDTO {
    @Schema(description = "메세지", example = "메세지 출력")
    private String message;
}
