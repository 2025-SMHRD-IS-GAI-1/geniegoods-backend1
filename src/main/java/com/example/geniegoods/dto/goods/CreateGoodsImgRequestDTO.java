package com.example.geniegoods.dto.goods;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CreateGoodsImgRequestDTO {
    private MultipartFile[] uploadImages;
    private Long prevUploadImgGroupId;
    private String prevGoodsImageUrl;
    private String description;
    private String category;
    private String style;
    private String color;
    private String mood;
}
