package com.example.geniegoods.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.geniegoods.entity.UploadImgEntity;
import com.example.geniegoods.entity.UploadImgGroupEntity;

public interface UploadImgRepository extends JpaRepository<UploadImgEntity, Long> {
    List<UploadImgEntity> findByUploadImgGroup(UploadImgGroupEntity uploadImgGroup);
}
