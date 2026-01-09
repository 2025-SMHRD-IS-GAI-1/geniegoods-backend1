package com.example.geniegoods.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GoodsMood {
    MINIMAL("미니멀"),
    CASUAL("캐주얼"),
    PREMIUM("고급");

    private final String koreanName;

    /**
     * 한글명으로 Enum 찾기
     */
    public static GoodsMood fromKoreanName(String koreanName) {
        for (GoodsMood mood : values()) {
            if (mood.koreanName.equals(koreanName)) {
                return mood;
            }
        }
        throw new IllegalArgumentException("존재하지 않는 분위기입니다: " + koreanName);
    }
}

