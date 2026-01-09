// com.example.geniegoods.dto.order.OrderRequestDto.java

package com.example.geniegoods.dto.order;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class OrderRequestDTO {

    // 검증 어노테이션 모두 제거 → 그냥 필드만 남김
    private List<OrderItemDto> items;

    private String zipcode;

    private String address;

    private String detailAddress;

    private String method;

    // 내부 클래스도 어노테이션 제거
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @ToString
    public static class OrderItemDto {

        private Long goodsId;       // 굿즈 ID

        private Integer quantity;   // 수량
    }
}