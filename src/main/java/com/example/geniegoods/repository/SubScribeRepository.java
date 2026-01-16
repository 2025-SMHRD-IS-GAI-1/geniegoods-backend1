package com.example.geniegoods.repository;

import com.example.geniegoods.entity.SubScribeEntity;
import com.example.geniegoods.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SubScribeRepository extends JpaRepository<SubScribeEntity, Long> {

    Optional<SubScribeEntity> findByUserAndStartDateBetween(UserEntity user, LocalDateTime minusMonths, LocalDateTime now);

    /**
     * 구독 기간이 만료된 사용자들의 구독 정보 조회
     * startDate + 1개월이 현재 시간보다 이전인 경우
     * @param now 현재 시간
     * @return 구독 기간이 만료된 구독 정보 리스트
     */
    @Query(value = "select * from\n" +
            "tb_subscribe s inner join tb_user u\n" +
            "on s.user_id = u.user_id\n" +
            "where u.subscription_plan = \"PRO\"\n" +
            "and date_add(s.start_date, interval 1 month) < :now", nativeQuery = true)
    List<SubScribeEntity> findExpiredSubscriptions(@Param("now") LocalDateTime now);
}
