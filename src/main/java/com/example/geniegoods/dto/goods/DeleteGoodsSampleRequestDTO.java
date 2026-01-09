package com.example.geniegoods.dto.goods;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class DeleteGoodsSampleRequestDTO {
    List<String> goodsSampleImgUrl;
}
