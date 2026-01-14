package com.example.geniegoods.dto.goods;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Schema(description = "굿즈 이미지 생성 요청")
public class CreateGoodsImgRequestDTO {
    @Schema(description = "업로드한 이미지들(2개)")
    private MultipartFile[] uploadImages;
    @Schema(description = "이전 업로드한 이미지 그룹 PK값", example = "1")
    private Long prevUploadImgGroupId;
    @Schema(description = "이전 업로드한 이미지 url", example = "https://kr.object.ncloudstorage.com")
    private String prevGoodsImageUrl;
    @Schema(description = "프롬프트", example = "사진속 객체를 화풍은 일러스트 색감은 차분 분위기는 미니멀 하게 핸드폰케이스로 만들어줘")
    private String description;
    @Schema(description = "카테고리", example = "(키링, 핸드폰케이스, 그립톡, 카드 지갑, 머그컵) 중 1개")
    private String category;
    @Schema(description = "스타일", example = "(일러스트, 실사, 페인팅) 중 1개")
    private String style;
    @Schema(description = "색감", example = "(따뜻, 차분, 비비드) 중 1개")
    private String color;
    @Schema(description = "분위기", example = "(미니멀, 캐주얼, 고급) 중 1개")
    private String mood;
}
