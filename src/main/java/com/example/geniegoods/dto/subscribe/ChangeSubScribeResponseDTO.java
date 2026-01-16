package com.example.geniegoods.dto.subscribe;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

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

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "구독 마감 기간", example = "2026-02-16T16:38:48")
    private LocalDateTime subscriptionExpiryDate;
}
