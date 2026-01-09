package com.example.geniegoods.dto.goods;

import java.util.List;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CreateGoodsSampleResponseDTO {
    private String status;
    private String message;
    private List<String> goodsSampleImgUrls; // URL 리스트로 변경
    private Long uploadImgGroupId;
}
