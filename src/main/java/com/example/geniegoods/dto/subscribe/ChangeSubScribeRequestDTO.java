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
public class ChangeSubScribeRequestDTO {
    @Schema(description = "구독 플랜 이름", example = "PRO")
    private String subscriptionPlan;

    @Schema(description = "결제 방법", example = "TOSSPAY")
    private String method;
}
