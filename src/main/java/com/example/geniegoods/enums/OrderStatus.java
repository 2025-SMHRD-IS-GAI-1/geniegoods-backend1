package com.example.geniegoods.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderStatus {
    ORDERED("주문완료"),
    DELIVERING("배송중"),
    DELIVERED("배송완료"),
    CANCELED("취소");

    private final String description;

    /**
     * 한글명으로 Enum 찾기
     */
    public static OrderStatus fromDescription(String description) {
        for (OrderStatus status : values()) {
            if (status.description.equals(description)) {
                return status;
            }
        }
        throw new IllegalArgumentException("존재하지 않는 주문상태입니다: " + description);
    }
}
