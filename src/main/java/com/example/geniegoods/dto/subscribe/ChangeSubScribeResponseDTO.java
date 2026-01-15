package com.example.geniegoods.dto.subscribe;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Schema(description = "구독 플랜 변경 응답")
public class ChangeSubScribeResponseDTO {
    @Schema(description = "구독 플랜 이름", example = "PRO")
    private String subscriptionPlan;
}
