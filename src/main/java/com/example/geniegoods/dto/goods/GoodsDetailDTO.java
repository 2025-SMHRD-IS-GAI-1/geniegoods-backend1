package com.example.geniegoods.dto.goods;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class GoodsDetailDTO {
    private String status;
    private String message;                   
}