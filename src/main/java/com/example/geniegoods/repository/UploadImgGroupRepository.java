package com.example.geniegoods.repository;

import com.example.geniegoods.entity.UploadImgGroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UploadImgGroupRepository extends JpaRepository<UploadImgGroupEntity, Long> {
}
