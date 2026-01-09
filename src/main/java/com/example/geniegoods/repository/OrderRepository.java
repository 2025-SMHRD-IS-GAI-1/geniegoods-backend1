package com.example.geniegoods.repository;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.geniegoods.entity.OrderEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    
    /**
     * 특정 접두사로 시작하는 주문번호 개수 조회
     * @param prefix 주문번호 접두사 (예: "20240512")
     * @return 해당 접두사로 시작하는 주문번호 개수
     */
    @Query("SELECT COUNT(o) FROM OrderEntity o WHERE o.orderNumber LIKE :prefix%")
    long countByOrderNumberStartingWith(@Param("prefix") String prefix);
    
 // 사용자별 주문 목록 (최근순)
    List<OrderEntity> findByUserUserIdOrderByOrderedAtDesc(Long userId);

    // 특정 주문 + 사용자 확인
    Optional<OrderEntity> findByOrderIdAndUserUserId(Long orderId, Long userId);
    
    long countByUserUserId(Long userId);

    List<OrderEntity> findByUserUserIdAndOrderedAtAfterOrderByOrderedAtDesc(Long userId, LocalDateTime orderedAtAfter);

    long countByUserUserIdAndOrderedAtAfter(Long userId, LocalDateTime orderedAtAfter);

    // Pageable을 사용한 페이징 메서드
    Page<OrderEntity> findByUserUserIdOrderByOrderedAtDesc(Long userId, Pageable pageable);

    Page<OrderEntity> findByUserUserIdAndOrderedAtAfterOrderByOrderedAtDesc(Long userId, LocalDateTime orderedAtAfter, Pageable pageable);
}