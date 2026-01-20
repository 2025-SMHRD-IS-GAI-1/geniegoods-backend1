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
public class DeleteGoodsSampleRequestDTO {
    @Schema(description = "굿즈 시안 url 리스트", example = "[https://kr.object.ncloudstorage.com, https://kr.object.ncloudstorage.com]")
    List<String> goodsSampleImgUrl;
}
