package com.example.geniegoods.dto.goods;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class GoodsBrowseDTO {
    private Long goodsId;
    private String goodsUrl;
    private Long viewCount;
}