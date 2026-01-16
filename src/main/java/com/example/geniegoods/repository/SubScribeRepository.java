package com.example.geniegoods.repository;

import com.example.geniegoods.entity.SubScribeEntity;
import com.example.geniegoods.entity.UserEntity;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SubScribeRepository extends JpaRepository<SubScribeEntity, Long> {

    Optional<SubScribeEntity> findByUserAndStartDateBetween(UserEntity user, LocalDateTime minusMonths, LocalDateTime now);
}
