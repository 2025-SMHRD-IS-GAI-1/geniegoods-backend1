package com.example.geniegoods.dto.order;

import com.example.geniegoods.entity.OrderEntity;
import com.example.geniegoods.entity.OrderItemEntity;
import lombok.*;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class AllOrderResponseDTO {
    private Long orderId;
    private String orderNumber;
    private String orderedAt;
    private String goodsUrl;
    private String orderTitle;
    private String status;           // 주문 상태
    private Integer totalAmount;      // 총 금액

    public static AllOrderResponseDTO of(OrderEntity order) {
        if (order == null || order.getOrderItems() == null || order.getOrderItems().isEmpty()) {
            throw new IllegalArgumentException("주문 정보가 없거나 주문 아이템이 없습니다.");
        }

        List<OrderItemEntity> orderItemList = order.getOrderItems();
        String orderTitle = orderItemList.getFirst().getGoods().getGoodsCategoryEntity().getKoreanName();

        // 2개 이상일 때 "외 N-1건" 형식으로 표시
        if (orderItemList.size() >= 2) {
            orderTitle += " 외 " + (orderItemList.size() - 1) + "건";
        }
        
        String orderedAt = order.getOrderedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        return AllOrderResponseDTO.builder()
                .orderId(order.getOrderId())
                .orderNumber(order.getOrderNumber())
                .orderedAt(orderedAt)
                .orderTitle(orderTitle)
                .goodsUrl(order.getOrderItems().getFirst().getGoods().getGoodsUrl())
                .status(order.getStatus() != null ? order.getStatus().getDescription() : null)
                .totalAmount(order.getTotalAmount())
                .build();
    }
}
