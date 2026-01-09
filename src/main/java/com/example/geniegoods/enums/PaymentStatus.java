package com.example.geniegoods.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentStatus {
    READY("결제요청"),
    IN_PROGRESS("결제진행"),
    PAID("결제완료"),
    FAILED("결제실패"),
    CANCELED("결제취소");

    private final String description;

    /**
     * 한글명으로 Enum 찾기
     */
    public static PaymentStatus fromDescription(String description) {
        for (PaymentStatus status : values()) {
            if (status.description.equals(description)) {
                return status;
            }
        }
        throw new IllegalArgumentException("존재하지 않는 색감입니다: " + description);
    }

}
