package com.example.geniegoods.service;

import com.example.geniegoods.dto.goods.CreateGoodsImgRequestDTO;
import com.example.geniegoods.entity.UploadImgEntity;
import com.example.geniegoods.entity.UploadImgGroupEntity;
import com.example.geniegoods.entity.UserEntity;
import com.example.geniegoods.repository.UploadImgRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UploadImgService {

    private final UploadImgRepository uploadImgRepository;


    /**
     * 업로드 이미지 MYSQL DB 저장
     * @param user
     * @param dto
     * @param uploadImgUrlList
     */
    public void uploadImgSave(UserEntity user, CreateGoodsImgRequestDTO dto, List<String> uploadImgUrlList, UploadImgGroupEntity uploadImgGroup) {

        // 기존에 업로드 이미지 그룹 아이디가 있을 경우 DB 값 삭제
        if(dto.getPrevUploadImgGroupId() != null) {
            List<UploadImgEntity> uploadImgEntityList = uploadImgRepository.findByUploadImgGroup(uploadImgGroup);
            for(UploadImgEntity uploadImgEntity : uploadImgEntityList) {
                uploadImgRepository.delete(uploadImgEntity);
            }
        }

        // 업로드 이미지 DB 저장
        if (dto.getUploadImages() != null) {
            for(int i = 0; i < dto.getUploadImages().length; i++) {
                UploadImgEntity uploadImgEntity = UploadImgEntity.builder()
                        .uploadImgUrl(uploadImgUrlList.get(i))
                        .uploadImgSize(dto.getUploadImages()[i].getSize())
                        .user(user)
                        .uploadImgGroup(uploadImgGroup)
                        .build();

                uploadImgRepository.save(uploadImgEntity);
            }
        }

    }


    /**
     * 업로드 이미지 id로 업로드 이미지 객체 찾기
     * @param uploadImgGroup
     * @return
     */
    public List<UploadImgEntity> getUploadImgEntity(UploadImgGroupEntity uploadImgGroup) {
        return uploadImgRepository.findByUploadImgGroup(uploadImgGroup);
    }
}
