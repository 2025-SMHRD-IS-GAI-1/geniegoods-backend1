package com.example.geniegoods.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.geniegoods.entity.OrderItemEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItemEntity, Long> {
}
