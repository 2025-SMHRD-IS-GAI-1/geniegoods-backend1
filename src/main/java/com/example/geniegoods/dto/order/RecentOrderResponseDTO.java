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
public class RecentOrderResponseDTO {

    private Long orderId;
    private String orderNumber;
    private String orderedAt;
    private String goodsUrl;
    private String orderTitle;

    public static RecentOrderResponseDTO of(OrderEntity order) {

        List<OrderItemEntity> orderItemList = order.getOrderItems();
        String orderTitle = orderItemList.getFirst().getGoods().getGoodsCategoryEntity().getKoreanName();

        if(orderItemList.size() >= 2) {
            orderTitle += " 외" + orderItemList.size();
        }
        String orderedAt = order.getOrderedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        return RecentOrderResponseDTO.builder()
                .orderId(order.getOrderId())
                .orderNumber(order.getOrderNumber())
                .orderedAt(orderedAt)
                .orderTitle(orderTitle)
                .goodsUrl(order.getOrderItems().getFirst().getGoods().getGoodsUrl())
                .build();
    }

}
