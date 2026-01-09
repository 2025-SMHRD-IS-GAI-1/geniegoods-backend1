package com.example.geniegoods.repository;

import com.example.geniegoods.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 사용자 엔티티 Repository
 */
@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
	
	/**
	 * 소셜 타입과 소셜 ID로 사용자 조회
	 * @param socialType 소셜 타입 (google, kakao, naver)
	 * @param socialId 소셜 ID
	 * @return 사용자 엔티티
	 */
	Optional<UserEntity> findBySocialTypeAndSocialId(String socialType, String socialId);

	/**
	 * 닉네임으로 사용자 조회
	 * @param nickname 닉네임
	 * @return 사용자 엔티티
	 */
    Optional<UserEntity> findByNickname(String nickname);
    Optional<UserEntity> findByUserId(Long userId);

    long countByNickname(String nickname);
}

