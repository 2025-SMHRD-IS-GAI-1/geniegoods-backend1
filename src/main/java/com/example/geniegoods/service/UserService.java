package com.example.geniegoods.service;

import com.example.geniegoods.entity.UserEntity;
import com.example.geniegoods.repository.GoodsRepository;
import com.example.geniegoods.repository.UserRepository;
import com.example.geniegoods.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final GoodsRepository goodsRepository;  // 굿즈 비공개 처리용
    private final JwtUtil jwtUtil;

    private static final int REJOIN_BLOCK_DAYS = 30;  // 재가입 제한 기간

    // 토큰 발급
    public Map<String, String> getToken(Long userId) {
        UserEntity user = findById(userId);

        String accessToken = jwtUtil.generateAccessToken(user.getUserId(), user.getNickname());
        Map<String, String> response = new HashMap<>();
        response.put("accessToken", accessToken);
        return response;
    }

    // 프로필 이미지 업데이트
    @Transactional
    public UserEntity updateProfileImage(UserEntity user, String imageUrl) {
        user.setProfileUrl(imageUrl);
        user.setUpdatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    // 닉네임 중복 확인
    public long isNicknameExists(String nickname) {
        return userRepository.countByNickname(nickname);
    }

    // 닉네임 업데이트 (컨트롤러에서 user.setNickname 후 호출)
    @Transactional
    public void updateNickname(UserEntity user) {
        userRepository.save(user);
    }

    // ===== 회원 탈퇴 메서드 (핵심!) =====
    @Transactional
    public void withdrawUser(Long userId) {
        UserEntity user = findById(userId);

        if (user.isDeleted()) {
            throw new IllegalStateException("이미 탈퇴 처리된 회원입니다.");
        }

        // 개인정보 마스킹 (복구 가능하도록 최소한으로)
        user.setNickname("탈퇴한 사용자");
        user.setProfileUrl(null);

        // 탈퇴 상태 변경
        user.setDeleted(true);
        user.setDeletedAt(LocalDateTime.now());

        userRepository.save(user);

        // 사용자가 만든 굿즈 비공개 처리
        goodsRepository.updateIsPublicByUserId(userId, false);
    }

    // ===== 소셜 로그인 시 재가입 제한 체크 및 복구 로직 =====
    @Transactional
    public UserEntity registerOrLogin(String socialType, String socialId, String nickname, String profileUrl) {
    	UserEntity user = userRepository.findBySocialTypeAndSocialId(socialType, socialId).orElse(null);

        if (user != null && user.isDeleted()) {
            LocalDateTime deletedAt = user.getDeletedAt();
            if (deletedAt == null) {
                deletedAt = LocalDateTime.now(); // 안전장치
            }

            LocalDateTime blockUntil = deletedAt.plusDays(REJOIN_BLOCK_DAYS);

            if (LocalDateTime.now().isBefore(blockUntil)) {
                long daysLeft = Duration.between(LocalDateTime.now(), blockUntil).toDays() + 1;
                throw new IllegalStateException(
                    "탈퇴한 계정입니다. " + daysLeft + "일 후에 재가입이 가능합니다."
                );
            }

            // 제한 기간 지남 → 자동 복구
            user.setDeleted(false);
            user.setDeletedAt(null);
            user.setNickname(nickname);           // 새 닉네임으로 복구
            user.setProfileUrl(profileUrl);
            // 굿즈 다시 공개 처리 (선택사항)
            goodsRepository.updateIsPublicByUserId(user.getUserId(), true);
            return userRepository.save(user);
        }

        // 신규 가입
        if (user == null) {
            user = UserEntity.builder()
                    .nickname(nickname)
                    .socialType(socialType)
                    .socialId(socialId)
                    .profileUrl(profileUrl)
                    .role("USER")
                    .deleted(false)
                    .build();
            return userRepository.save(user);
        }

        // 기존 정상 사용자
        return user;
    }

    // ===== 공용 조회 메서드 =====
    public UserEntity findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
    }

    public UserEntity findByNickname(String nickname) {
        return userRepository.findByNickname(nickname)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
    }
}