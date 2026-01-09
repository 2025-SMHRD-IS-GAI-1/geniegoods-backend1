// com.example.geniegoods.dto.order.OrderResponseDto.java

package com.example.geniegoods.dto.order;

import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class OrderResponseDTO {

    private Long orderId;                    // TB_ORDER.ORDER_ID
    private String orderNumber;              // 주문번호 (예: 20240512-001)
    private LocalDateTime orderedAt;         // 주문일시
    private Integer totalAmount;             // 총 금액 (상품 합계 + 배송비)
    private String status;                   // 주문 상태 (PENDING, PAID 등)
    private String zipcode;
    private String address;
    private String detailAddress;
    
    private Integer subtotal;       // 상품 합계 (itemTotal들의 합)
    private Integer shippingFee;    // 배송비 (고정 3000원)
    private Integer finalAmount;    // subtotal + shippingFee (totalAmount와 동일)

    private List<OrderItemResponseDto> items;

    // 주문 상품 하나당 정보
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @ToString
    public static class OrderItemResponseDto {

        private Long orderItemId;
        private Long goodsId;
        private String goodsUrl;              // 상품 이미지 URL (프론트에서 바로 보여줄 수 있음)
        private String goodsStyle;
        private String goodsTone;
        private String goodsMood;
        private String categoryKoreanName;    // 예: "키링", "폰케이스"
        private Integer quantity;             // 주문 수량
        private Integer priceAtOrder;         // 주문 당시 단가 (TB_ORDER_ITEM.PRICE_AT_ORDER)
        private Integer itemTotal;            // 수량 × 단가 (프론트 계산 편하게)
    }
}