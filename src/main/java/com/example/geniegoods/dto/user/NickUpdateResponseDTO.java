package com.example.geniegoods.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Schema(description = "닉네임 변경 응답")
public class NickUpdateResponseDTO {

    @Schema(description = "메세지", example = "이미 사용 중인 닉네임입니다.")
    private String message;
}
