package com.example.geniegoods.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * JWT 토큰 생성 및 검증 유틸리티
 */
@Component
public class JwtUtil {

	private final SecretKey secretKey;
	private final long accessTokenExpiration;
	private final long refreshTokenExpiration;

	public JwtUtil(
			@Value("${app.auth.token-secret}") String tokenSecret,
			@Value("${app.auth.access-token-expiration-msec}") long accessTokenExpiration,
			@Value("${app.auth.refresh-token-expiration-msec}") long refreshTokenExpiration) {
		this.secretKey = Keys.hmacShaKeyFor(tokenSecret.getBytes(StandardCharsets.UTF_8));
		this.accessTokenExpiration = accessTokenExpiration;
		this.refreshTokenExpiration = refreshTokenExpiration;
	}

	/**
	 * JWT Access Token 생성
	 * @param userId 사용자 ID
	 * @param nickname 사용자 닉네임
	 * @return JWT 토큰 문자열
	 */
	public String generateAccessToken(Long userId, String nickname) {
		Date now = new Date();
		Date expiryDate = new Date(now.getTime() + accessTokenExpiration);

		return Jwts.builder()
				.subject(String.valueOf(userId))
				.claim("nickname", nickname)
				.claim("type", "access")
				.issuedAt(now)
				.expiration(expiryDate)
				.signWith(secretKey)
				.compact();
	}

	/**
	 * JWT Refresh Token 생성
	 * @param userId 사용자 ID
	 * @return JWT refresh 토큰 문자열
	 */
	public String generateRefreshToken(Long userId) {
		Date now = new Date();
		Date expiryDate = new Date(now.getTime() + refreshTokenExpiration);

		return Jwts.builder()
				.subject(String.valueOf(userId))
				.claim("type", "refresh")
				.issuedAt(now)
				.expiration(expiryDate)
				.signWith(secretKey)
				.compact();
	}

	/**
	 * JWT 토큰에서 사용자 ID 추출
	 * @param token JWT 토큰
	 * @return 사용자 ID
	 */
	public Long getUserIdFromToken(String token) {
		Claims claims = getClaimsFromToken(token);
		return Long.parseLong(claims.getSubject());
	}

	/**
	 * JWT 토큰에서 닉네임 추출
	 * @param token JWT 토큰
	 * @return 닉네임
	 */
	public String getNicknameFromToken(String token) {
		Claims claims = getClaimsFromToken(token);
		return claims.get("nickname", String.class);
	}

	/**
	 * JWT 토큰 유효성 검증
	 * @param token JWT 토큰
	 * @return 유효 여부
	 */
	public boolean validateToken(String token) {
		try {
			getClaimsFromToken(token);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * JWT 토큰에서 Claims 추출
	 * @param token JWT 토큰
	 * @return Claims
	 */
	private Claims getClaimsFromToken(String token) {
		return Jwts.parser()
				.verifyWith(secretKey)
				.build()
				.parseSignedClaims(token)
				.getPayload();
	}
}

