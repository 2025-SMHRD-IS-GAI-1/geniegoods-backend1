// com.example.geniegoods.dto.order.OrderResponseDto.java

package com.example.geniegoods.dto.order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Schema(description = "주문 상세 응답")
public class OrderResponseDTO {

    @Schema(description = "주문 PK", example = "1")
    private Long orderId;                    // TB_ORDER.ORDER_ID

    @Schema(description = "주문번호", example = "20240512-001")
    private String orderNumber;              // 주문번호 (예: 20240512-001)

    @Schema(description = "주문일시", example = "2026-01-08 14:30:49.367708")
    private LocalDateTime orderedAt;         // 주문일시

    @Schema(description = "총금액", example = "23000")
    private Integer totalAmount;             // 총 금액 (상품 합계 + 배송비)

    @Schema(description = "주문 상태", example = "주문완료")
    private String status;

    @Schema(description = "우편코드", example = "06035")
    private String zipcode;

    @Schema(description = "주소", example = "서울 강남구 가로수길 9 (신사동)")
    private String address;

    @Schema(description = "상세주소", example = "103호")
    private String detailAddress;

    @Schema(description = "상품 합계", example = "14000")
    private Integer subtotal;       // 상품 합계 (itemTotal들의 합)

    @Schema(description = "배송비", example = "3000")
    private Integer shippingFee;    // 배송비 (고정 3000원)

    @Schema(description = "주문 상품 하나당 정보", example = "3000")
    private List<OrderItemResponseDto> items;

    // 주문 상품 하나당 정보
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @ToString
    public static class OrderItemResponseDto {

        @Schema(description = "주문 상품 PK", example = "1")
        private Long orderItemId;

        @Schema(description = "주문 PK", example = "1")
        private Long goodsId;

        @Schema(description = "굿즈 이미지 url", example = "https://kr.object.ncloudstorage.com")
        private String goodsUrl;

        @Schema(description = "스타일", example = "(일러스트, 실사, 페인팅) 중 1개")
        private String goodsStyle;

        @Schema(description = "색감", example = "(따뜻, 차분, 비비드) 중 1개")
        private String goodsTone;

        @Schema(description = "분위기", example = "(미니멀, 캐주얼, 고급) 중 1개")
        private String goodsMood;

        @Schema(description = "카테고리", example = "(키링, 핸드폰케이스, 그립톡, 카드 지갑, 머그컵) 중 1개")
        private String categoryKoreanName;

        @Schema(description = "주문 수량", example = "1")
        private Integer quantity;

        @Schema(description = "주문 당시 단가", example = "5000")
        private Integer priceAtOrder;

        @Schema(description = "수량 x 단가", example = "34000")
        private Integer itemTotal;
    }
}