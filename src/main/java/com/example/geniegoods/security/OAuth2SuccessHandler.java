package com.example.geniegoods.security;

import com.example.geniegoods.entity.UserEntity;
import com.example.geniegoods.service.OAuth2UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * OAuth2 로그인 성공 핸들러
 * 소셜 로그인 성공 시 JWT 토큰을 생성하고 프론트엔드로 리다이렉트
 */
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	private final OAuth2UserService oAuth2UserService;
	private final JwtUtil jwtUtil;
	
	// 프론트엔드 URL
	private static final String FRONTEND_URL = "http://localhost:5173";

	@Override
	public void onAuthenticationSuccess(
			HttpServletRequest request,
			HttpServletResponse response,
			Authentication authentication) throws IOException {
		
		try {
			// OAuth2 사용자 정보 추출
			OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
			
			// registrationId 추출 (google, kakao, naver)
			String registrationId = getRegistrationId(request);
			
			// 사용자 정보 처리 (생성 또는 업데이트)
			UserEntity user = oAuth2UserService.processOAuth2User(oauth2User, registrationId);
			
			// JWT 토큰 생성
			String accessToken = jwtUtil.generateAccessToken(user.getUserId(), user.getNickname());
			
			// 프론트엔드로 리다이렉트 (토큰을 쿼리 파라미터로 전달)
			String redirectUrl = String.format("%s/oauth/callback?token=%s", FRONTEND_URL, accessToken);
			getRedirectStrategy().sendRedirect(request, response, redirectUrl);
		} catch (IllegalStateException e) {
			// 탈퇴한 계정 등의 비즈니스 로직 예외 처리
			String errorMessage = java.net.URLEncoder.encode(e.getMessage(), "UTF-8");
			String redirectUrl = String.format("%s/oauth/callback?error=%s", FRONTEND_URL, errorMessage);
			getRedirectStrategy().sendRedirect(request, response, redirectUrl);
		} catch (Exception e) {
			// 기타 예외 처리
			String errorMessage = java.net.URLEncoder.encode("로그인 처리 중 오류가 발생했습니다.", "UTF-8");
			String redirectUrl = String.format("%s/oauth/callback?error=%s", FRONTEND_URL, errorMessage);
			getRedirectStrategy().sendRedirect(request, response, redirectUrl);
		}
	}

	/**
	 * 요청 URL에서 registrationId 추출
	 */
	private String getRegistrationId(HttpServletRequest request) {
		String requestURI = request.getRequestURI();
		// /login/oauth2/code/{registrationId} 형식에서 추출
		String[] parts = requestURI.split("/");
		return parts[parts.length - 1];
	}
}

