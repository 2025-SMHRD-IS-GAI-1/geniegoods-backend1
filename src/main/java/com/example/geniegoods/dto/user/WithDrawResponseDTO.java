package com.example.geniegoods.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Schema(description = "회원 탈퇴 응답")
public class WithDrawResponseDTO {

    @Schema(description = "상태 값", example = "SUCCESS")
    private String status;

    @Schema(description = "메세지", example = "로그인된 사용자가 없습니다.")
    private String message;
}
