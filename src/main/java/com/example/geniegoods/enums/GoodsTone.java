package com.example.geniegoods.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GoodsTone {
    WARM("따뜻"),
    CALM("차분"),
    VIVID("비비드");

    private final String koreanName;

    /**
     * 한글명으로 Enum 찾기
     */
    public static GoodsTone fromKoreanName(String koreanName) {
        for (GoodsTone tone : values()) {
            if (tone.koreanName.equals(koreanName)) {
                return tone;
            }
        }
        throw new IllegalArgumentException("존재하지 않는 색감입니다: " + koreanName);
    }
}

