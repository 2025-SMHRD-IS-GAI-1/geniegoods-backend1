package com.example.geniegoods.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Schema(description = "프로필 이미지 응답")
public class ProfileImgResponseDTO {

    @Schema(description = "상태 값", example = "SUCCESS")
    private String status;

    @Schema(description = "메세지", example = "프로필 이미지가 업로드되었습니다.")
    private String message;

    @Schema(description = "프로필 이미지 응답", example = "https://kr.object.ncloudstorage.com")
    private String profileUrl;
}
