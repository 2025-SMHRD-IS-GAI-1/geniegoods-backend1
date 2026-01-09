package com.example.geniegoods.service;

import com.example.geniegoods.entity.UploadImgGroupEntity;
import com.example.geniegoods.repository.UploadImgGroupRepository;
import com.example.geniegoods.repository.UploadImgRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UploadImgGroupService {
    private final UploadImgGroupRepository uploadImgGroupRepository;

    public UploadImgGroupEntity getUploadImgGroupEntity(Long uploadImgGroupId) {
        return uploadImgGroupRepository.findById(uploadImgGroupId).orElse(null);
    }

    public UploadImgGroupEntity createUploadImgGroupEntity() {

        UploadImgGroupEntity uploadImgGroupEntity = UploadImgGroupEntity.builder().build();
        uploadImgGroupRepository.save(uploadImgGroupEntity);

        return uploadImgGroupEntity;

    }


}
