package com.example.geniegoods.dto.user;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Schema(description = "닉네임 중복 확인 응답")
public class NickCheckResponseDTO {
    @Schema(description = "사용 가능 여부", example = "true")
    private boolean available;
    @Schema(description = "결과 메세지", example = "사용 가능한 닉네임입니다.")
    private String message;
}
