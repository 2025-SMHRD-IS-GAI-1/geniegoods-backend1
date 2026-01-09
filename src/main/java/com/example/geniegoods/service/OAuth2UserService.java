package com.example.geniegoods.service;

import com.example.geniegoods.entity.UserEntity;
import com.example.geniegoods.repository.GoodsRepository;
import com.example.geniegoods.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OAuth2UserService {

    private final UserRepository userRepository;
    private final GoodsRepository goodsRepository;

    /**
     * OAuth2 사용자 정보로부터 사용자 엔티티 생성 또는 조회 (탈퇴 계정 복구 포함)
     */
    @Transactional
    public UserEntity processOAuth2User(OAuth2User oauth2User, String registrationId) {
        String socialType = registrationId.toUpperCase();
        String socialId = extractSocialId(oauth2User, registrationId);

        Optional<UserEntity> existingUserOpt = userRepository.findBySocialTypeAndSocialId(socialType, socialId);

        if (existingUserOpt.isPresent()) {
            UserEntity user = existingUserOpt.get();

            // 탈퇴한 계정일 경우 재가입 제한 체크
            if (user.isDeleted()) {
                LocalDateTime deletedAt = user.getDeletedAt();
                if (deletedAt == null) {
                    deletedAt = LocalDateTime.now(); // 안전장치
                }

                LocalDateTime blockUntil = deletedAt.plusDays(30);  // 30일 제한

                if (LocalDateTime.now().isBefore(blockUntil)) {
                    long daysLeft = Duration.between(LocalDateTime.now(), blockUntil).toDays() + 1;
                    throw new IllegalStateException(
                        "탈퇴한 계정입니다. " + daysLeft + "일 후에 재가입이 가능합니다."
                    );
                }

                // 30일 지남 → 자동 복구
                String newNickname = extractUserInfo(oauth2User, registrationId);
                String newProfileUrl = extractProfileUrl(oauth2User, registrationId);

                user.setDeleted(false);
                user.setDeletedAt(null);
                user.setNickname(newNickname);
                user.setProfileUrl(newProfileUrl);
                user.setUpdatedAt(LocalDateTime.now());

                // 굿즈 다시 공개 처리 (선택사항, GoodsRepository에 메서드 있어야 함)
                goodsRepository.updateIsPublicByUserId(user.getUserId(), true);

                return userRepository.save(user);
            }

            return user;
        }

        // 신규 사용자 생성
        return createNewUser(oauth2User, socialType, socialId, registrationId);
    }

    // 기존 메서드들 (extractSocialId, extractUserInfo, createNewUser) 그대로 유지

    private String extractSocialId(OAuth2User oauth2User, String registrationId) {
        Map<String, Object> attributes = oauth2User.getAttributes();
        return switch (registrationId.toLowerCase()) {
            case "google" -> (String) attributes.get("sub");
            case "kakao" -> String.valueOf(attributes.get("id"));
            case "naver" -> {
                Map<String, Object> response = (Map<String, Object>) attributes.get("response");
                yield (String) response.get("id");
            }
            default -> throw new IllegalArgumentException("지원하지 않는 소셜 로그인입니다: " + registrationId);
        };
    }

    private String extractUserInfo(OAuth2User oauth2User, String registrationId) {
        Map<String, Object> attributes = oauth2User.getAttributes();
        return switch (registrationId.toLowerCase()) {
            case "google" -> (String) attributes.get("name");
            case "kakao" -> {
                Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
                Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
                yield (String) profile.get("nickname");
            }
            case "naver" -> {
                Map<String, Object> response = (Map<String, Object>) attributes.get("response");
                yield (String) response.get("name");
            }
            default -> throw new IllegalArgumentException("지원하지 않는 소셜 로그인입니다: " + registrationId);
        };
    }

    // 프로필 이미지 URL 추출 추가 (kakao만 있음, google/naver는 보통 picture/response.profile_image_url)
    private String extractProfileUrl(OAuth2User oauth2User, String registrationId) {
        Map<String, Object> attributes = oauth2User.getAttributes();
        return switch (registrationId.toLowerCase()) {
            case "google" -> (String) attributes.get("picture");
            case "kakao" -> {
                Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
                Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
                yield (String) profile.get("profile_image_url");
            }
            case "naver" -> {
                Map<String, Object> response = (Map<String, Object>) attributes.get("response");
                yield (String) response.get("profile_image");
            }
            default -> null;
        };
    }

    private UserEntity createNewUser(OAuth2User oauth2User, String socialType, String socialId, String registrationId) {
        String nickname = extractUserInfo(oauth2User, registrationId);
        String profileUrl = extractProfileUrl(oauth2User, registrationId);

        UserEntity newUser = UserEntity.builder()
                .nickname(nickname)
                .socialType(socialType)
                .socialId(socialId)
                .profileUrl(profileUrl)
                .role("USER")
                .createdAt(LocalDateTime.now())
                .build();
        return userRepository.save(newUser);
    }
}