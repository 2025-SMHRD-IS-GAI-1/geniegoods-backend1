package com.example.geniegoods.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Schema(description = "닉네임 변경 요청")
public class NickUpdateRequestDTO {

    @Schema(description = "변경할 닉네임", example = "사과")
    private String nickname;
}