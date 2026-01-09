package com.example.geniegoods.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum PaymentMethod {
    TOSSPAY("토스페이"),
    KAKAOPAY("카카오페이"),
    CARD("신용/체크카드"),
    PHONE("휴대폰결제");

    private final String description;

    public static PaymentMethod from(String value) {
        if (value == null) {
            throw new IllegalArgumentException("PaymentMethod is null");
        }

        return Arrays.stream(values())
                .filter(method -> method.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException("Invalid PaymentMethod: " + value)
                );
    }
}
