package com.example.geniegoods.dto.order;

import com.example.geniegoods.entity.OrderEntity;
import com.example.geniegoods.entity.OrderItemEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Schema(description = "최근 주문내역 2건 조회 응답")
public class RecentOrderResponseDTO {

    @Schema(description = "주문 PK", example = "1")
    private Long orderId;

    @Schema(description = "주문번호", example = "#20260114-001")
    private String orderNumber;

    @Schema(description = "주문 날짜", example = "2026년 1월 9일")
    private String orderedAt;

    @Schema(description = "대표 굿즈 이미지 url", example = "https://kr.object.ncloudstorage.com")
    private String goodsUrl;

    @Schema(description = "주문 제목", example = "카드 지갑 외 2개")
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
