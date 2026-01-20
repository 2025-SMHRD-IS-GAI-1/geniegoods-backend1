package com.example.geniegoods.dto.goods;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CreateGoodsSampleResponseDTO {
    @Schema(description = "메세지", example = "시안 생성 완료")
    private String message;
    @Schema(description = "굿즈 이미지 url 리스트", example = "[https://kr.object.ncloudstorage.com, https://kr.object.ncloudstorage.com]")
    private List<String> goodsSampleImgUrls; // URL 리스트로 변경
    @Schema(description = "업로드 그룹 이미지 pk", example = "1")
    private Long uploadImgGroupId;
}
