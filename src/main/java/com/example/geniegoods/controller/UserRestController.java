package com.example.geniegoods.controller;

import com.example.geniegoods.dto.user.*;
import com.example.geniegoods.entity.UserEntity;
import com.example.geniegoods.service.ObjectStorageService;
import com.example.geniegoods.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Slf4j
public class UserRestController {

    private final UserService userService;

    private final ObjectStorageService objectStorageService;

    /**
     * 현재 로그인한 사용자 정보 조회
     */
    @GetMapping("/me")
    public ResponseEntity<UserEntity> getCurrentUser(@AuthenticationPrincipal UserEntity user) {
        return ResponseEntity.ok(user);
    }

    /**
     * 닉네임 중복 확인
     */
    @GetMapping("/nickname/check")
    public ResponseEntity<NickCheckResponseDTO> checkNicknameDuplicate(
            @RequestParam("nickname") String nickname,
            @AuthenticationPrincipal UserEntity currentUser) {

        NickCheckResponseDTO response = new NickCheckResponseDTO();

        // 현재 사용자의 닉네임과 같으면 사용 가능
        if (currentUser != null && currentUser.getNickname().equals(nickname)) {
            response.setAvailable(false);
            response.setMessage("현재 사용 중인 닉네임입니다.");
            return ResponseEntity.ok(response);
        }

        // 닉네임 중복 확인
        boolean available = true;
        String message = "사용 가능한 닉네임입니다.";
        long count = userService.isNicknameExists(nickname);
        if (count > 0) {
            available = false;
            message = "이미 사용 중인 닉네임입니다.";
        }

        response.setAvailable(available);
        response.setMessage(message);

        return ResponseEntity.ok(response);
    }

    /**
     * 닉네임 변경
     */
    @PatchMapping("/nickname/update")
    public ResponseEntity<NickUpdateResponseDTO> updateNickname(
            @RequestBody NickUpdateRequestDTO request,
            @AuthenticationPrincipal UserEntity currentUser) {

        String newNickname = request.getNickname();

        NickUpdateResponseDTO response = new NickUpdateResponseDTO();

        // validation
        // 프론트쪽에서 무슨 요청을 보낼지 모르니 모든 경우의 수를 막아야 함
        // 현재 사용자의 닉네임과 같으면 변경 안해도됨
        if (currentUser != null && currentUser.getNickname().equals(newNickname)) {
            response.setStatus("SAME_AS_CURRENT");
            response.setMessage("현재 사용 중인 닉네임입니다.");
            return ResponseEntity.ok(response);
        }

        // 닉네임 중복 확인
        long count = userService.isNicknameExists(newNickname);

        // 중복확인 했는데 있으면 변경 안해도됨
        if(count > 0) {
            response.setStatus("DUPLICATED");
            response.setMessage("이미 사용 중인 닉네임입니다.");
            return ResponseEntity.ok(response);
        } else { // 실제로 닉네임 변경
            currentUser.setNickname(newNickname);
            userService.updateNickname(currentUser);
        }

        response.setStatus("SUCCESS");
        response.setMessage("닉네임을 " + newNickname + "으로 변경했습니다.");

        return ResponseEntity.ok(response);
    }

    /**
     * 토큰 발급 (포스트맨 테스트용)
     */
    @GetMapping("/token/{userId}")
    public ResponseEntity<Map<String, String>> getToken(@PathVariable("userId") Long userId) {
        Map<String, String> response = userService.getToken(userId);
        return ResponseEntity.ok(response);
    }

    /**
     * 프로필 이미지 업로드
     */
    @PostMapping("/profile-image")
    public ResponseEntity<ProfileImgResponseDTO> uploadProfileImage(
            @AuthenticationPrincipal UserEntity user,
            @RequestParam("file") MultipartFile file) {
        try {
            ProfileImgResponseDTO response = new ProfileImgResponseDTO();
            // 파일 유효성 검사
            if (file.isEmpty()) {
                response.setStatus("ERROR");
                response.setMessage("파일이 비어있습니다.");
                return ResponseEntity.badRequest().body(response);
            }

            // 이미지 파일인지 확인
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                response.setStatus("ERROR");
                response.setMessage("이미지 파일만 업로드 가능합니다.");
                return ResponseEntity.badRequest().body(response);
            }

            // 파일 크기 제한 (10MB)
            if (file.getSize() > 10 * 1024 * 1024) {
                response.setStatus("ERROR");
                response.setMessage("파일 크기는 10MB를 초과할 수 없습니다.");
                return ResponseEntity.badRequest().body(response);
            }

            // 기존 프로필 이미지가 있으면 삭제
            if (user.getProfileUrl() != null && !user.getProfileUrl().isEmpty()) {
                objectStorageService.deleteImage(user.getProfileUrl());
            }

            // 새로운 프로필 이미지 업로드
            String imageUrl = objectStorageService.uploadFile(file, user.getUserId(), "profile");

            // 프로필 이미지 업데이트
            UserEntity updatedUser = userService.updateProfileImage(user, imageUrl);

            response.setStatus("SUCCESS");
            response.setMessage("프로필 이미지가 업로드되었습니다.");
            response.setProfileUrl(updatedUser.getProfileUrl());

            return ResponseEntity.ok(response);
        } catch (IOException e) {
            ProfileImgResponseDTO response = new ProfileImgResponseDTO();
            response.setStatus("ERROR");
            response.setMessage("파일 업로드 중 오류가 발생했습니다: \" + e.getMessage()");
            return ResponseEntity.internalServerError().body(response);
        }
    }
  
  	/**
	 * 회원탈퇴
	 */
	@DeleteMapping("/me/withdraw")
	public ResponseEntity<WithDrawResponseDTO> withdraw(
	        @AuthenticationPrincipal UserEntity currentUser) {

        WithDrawResponseDTO response = new WithDrawResponseDTO();

	    if (currentUser == null) {
            response.setStatus("ERROR");
            response.setMessage("로그인된 사용자가 없습니다.");
	        return ResponseEntity.badRequest().body(response);
	    }

	    if (currentUser.isDeleted()) {
            response.setStatus("ERROR");
            response.setMessage("이미 탈퇴 처리된 계정입니다.");
	        return ResponseEntity.badRequest().body(response);
	    }

	    try {
	        userService.withdrawUser(currentUser.getUserId());
            response.setStatus("SUCCESS");
            response.setMessage("회원탈퇴가 완료되었습니다. 이용해 주셔서 감사합니다.");
	        return ResponseEntity.ok(response);
	    } catch (Exception e) {
	        log.error("회원탈퇴 처리 중 오류 발생", e);
            response.setStatus("ERROR");
            response.setMessage("탈퇴 처리 중 오류가 발생했습니다.");
	        return ResponseEntity.internalServerError().body(response);
	    }
	}

}
