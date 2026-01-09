package com.example.geniegoods.dto.goods;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CreateGoodsSampleRequestDTO {
    private String resultImageUrl; // Object Storage URL (우선 사용)
    private String description;
    private String category;
    private String style;
    private String color;
    private String mood;
    private Long uploadImgGroupId;
}
