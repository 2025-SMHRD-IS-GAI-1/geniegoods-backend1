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
@Schema(description = "굿즈 시안 선택 요청")
public class SelectGoodsRequestDTO {
    @Schema(description = "굿즈 결과 이미지 url", example = "https://kr.object.ncloudstorage.com")
    private String resultImageUrl;
    @Schema(description = "굿즈 시안 3개 이미지 url", example = "https://kr.object.ncloudstorage.com")
    private List<String> sampleGoodsImageUrl;
    @Schema(description = "굿즈 시안 선택 이미지 url", example = "https://kr.object.ncloudstorage.com")
    private String goodsImgUrl;
    @Schema(description = "프롬프트", example = "사진속 객체를 화풍은 일러스트 색감은 차분 분위기는 미니멀 하게 핸드폰케이스로 만들어줘")
    private String prompt;
    @Schema(description = "카테고리", example = "(키링, 핸드폰케이스, 그립톡, 카드 지갑, 머그컵) 중 1개")
    private String category;
    @Schema(description = "업로드 이미지 그룹 id", example = "1")
    private Long uploadImgGroupId;
    @Schema(description = "스타일", example = "(일러스트, 실사, 페인팅) 중 1개")
    private String goodsStyle;
    @Schema(description = "색감", example = "(따뜻, 차분, 비비드) 중 1개")
    private String goodsTone;
    @Schema(description = "분위기", example = "(미니멀, 캐주얼, 고급) 중 1개")
    private String goodsMood;
}
