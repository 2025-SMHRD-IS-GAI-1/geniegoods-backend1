package com.example.geniegoods.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GoodsStyle {
    ILLUSTRATION("일러스트"),
    REALISTIC("실사"),
    PAINTING("페인팅");

    private final String koreanName;

    /**
     * 한글명으로 Enum 찾기
     */
    public static GoodsStyle fromKoreanName(String koreanName) {
        for (GoodsStyle style : values()) {
            if (style.koreanName.equals(koreanName)) {
                return style;
            }
        }
        throw new IllegalArgumentException("존재하지 않는 화풍입니다: " + koreanName);
    }
}

