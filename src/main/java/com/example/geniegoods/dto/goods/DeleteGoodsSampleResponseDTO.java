package com.example.geniegoods.dto.goods;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class DeleteGoodsSampleResponseDTO {
    @Schema(description = "메세지", example = "https://kr.object.ncloudstorage.com")
    private String message;
}
