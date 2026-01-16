package com.example.geniegoods.dto.user;

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
@Schema(description = "헤더에 나타낼 유저 정보 응답")
public class CurrentUserResponseDTO {
    @Schema(description = "유저 닉네임", example = "나무")
    private String nickname;

    @Schema(description = "유저 프로필 url", example = "true")
    private String profileUrl;

    @Schema(description = "유저 구독 플랜", example = "FREE")
    private String subscriptionPlan;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "유저 구독 기간", example = "2026-02-16 11:05:44")
    private LocalDateTime subscriptionExpiryDate;
}
