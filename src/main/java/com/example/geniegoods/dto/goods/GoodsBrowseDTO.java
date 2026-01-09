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
    private String categoryKoreanName;
    private Long viewCount;
    private String creatorNickname;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;
    
    private String goodsStyle;
    private String goodsTone;
    private String goodsMood;
    private String prompt;
}