package com.example.geniegoods.controller;


import com.example.geniegoods.dto.goods.*;
import com.example.geniegoods.entity.UploadImgEntity;
import com.example.geniegoods.entity.UploadImgGroupEntity;
import com.example.geniegoods.entity.UserEntity;
import com.example.geniegoods.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Slf4j
@RestController
@RequestMapping("/api/goods")
@RequiredArgsConstructor
public class GoodsRestController {

    private final GoodsService goodsService;

    private final UploadImgService uploadImgService;

    private final UploadImgGroupService uploadImgGroupService;

    private final ObjectStorageService objectStorageService;

    private final YoloService yoloService;

    private final NanoService nanoService;

    /**
     * 선택된 굿즈 일괄 삭제 (이미지 포함)
     */
    @DeleteMapping("/bulk")
    public ResponseEntity<Map<String, String>> deleteGoodsBulk(
            @AuthenticationPrincipal UserEntity currentUser,
            @RequestParam List<Long> goodsIds) {

        if (goodsIds == null || goodsIds.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "삭제할 굿즈 ID가 필요합니다."));
        }

        try {
            goodsService.deleteGoodsByIds(goodsIds, currentUser.getUserId());
            return ResponseEntity.ok()
                    .body(Map.of("message", "선택된 굿즈와 이미지가 완전히 삭제되었습니다."));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(403)
                    .body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            log.error("굿즈 삭제 중 오류 발생", e);
            return ResponseEntity.internalServerError()
                    .body(Map.of("message", "삭제 처리 중 오류가 발생했습니다."));
        }
    }
    

    /**
     * 굿즈 이미지 생성
     * @param user 생성한 사용자
     * @param dto 굿즈 옵션 값
     * @return 프론트에 나노바나나로 생성한 이미지 url 지금은 임시방편으로 String 값 반환
     */
    @PostMapping("/create-image")
    public ResponseEntity<CreateGoodsImgResponseDTO> createGoodsImage(
            @AuthenticationPrincipal UserEntity user,
            @ModelAttribute CreateGoodsImgRequestDTO dto
    ) {

        List<String> uploadImgUrlList = new ArrayList<>();

        CreateGoodsImgResponseDTO responseDTO = new CreateGoodsImgResponseDTO();

        UploadImgGroupEntity uploadImgGroup = null;

        // 기존에 이미 업로드 이미지가 없으면 PK 생성
        if(dto.getPrevUploadImgGroupId() == null) {
            // 업로드 이미지 그룹 PK 생성
            uploadImgGroup = uploadImgGroupService.createUploadImgGroupEntity();
        } else {
            // 업로드 이미지 그룹 찾기
            uploadImgGroup = uploadImgGroupService.getUploadImgGroupEntity(dto.getPrevUploadImgGroupId());
        }

        // 기존에 이미 업로드 이미지가 있으면 Object Storage 에 있는 uploadImage 삭제
        if (dto.getPrevUploadImgGroupId() != null) {
            List<UploadImgEntity> uploadImg = uploadImgService.getUploadImgEntity(uploadImgGroup);
            for (UploadImgEntity uploadImgEntity : uploadImg) {
                objectStorageService.deleteImage(uploadImgEntity.getUploadImgUrl());
            }
        }

        // 업로드 이미지 Object Storage 저장
        if (dto.getUploadImages() != null) {
            for (MultipartFile file : dto.getUploadImages()) {
                try {
                    uploadImgUrlList.add(objectStorageService.uploadFile(file, user.getUserId(),
                            "upload" + "/" + user.getUserId() + "/" + uploadImgGroup.getUploadGroupId()));
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    responseDTO.setStatus("ERROR");
                    responseDTO.setMessage("업로드 이미지 Object Storage 저장 실패");
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
                }
            }
        }

        // 업로드 이미지 Mysql DB 저장
        uploadImgService.uploadImgSave(user, dto, uploadImgUrlList, uploadImgGroup);

        // yolo api 호출 -> 객체 이미지 생성
        List<byte[]> objectImgBytesList = yoloService.createObjectDetetctionImage(dto.getUploadImages());

        // 나노바나나 api 호출 -> 굿즈 이미지 생성
        MultipartFile goodsImgFile = nanoService.createGoodsImage(objectImgBytesList, dto);

        // 굿즈 이미지 파일 Object Storage 저장
        String goodsImgUrl = "";

        // 굿즈 이미지 이미 Object Storage에 저장되어있으면 삭제
        if(dto.getPrevGoodsImageUrl() != null) {
            objectStorageService.deleteImage(dto.getPrevGoodsImageUrl());
        }

        try {
            goodsImgUrl = objectStorageService.uploadFile(goodsImgFile, user.getUserId(), "temp" + "/" + user.getUserId() + "/" + uploadImgGroup.getUploadGroupId());
        } catch (IOException e) {
            responseDTO.setStatus("ERROR");
            responseDTO.setMessage("굿즈 이미지 Object Storage 저장 실패");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
        }

        MultipartFile file = dto.getUploadImages()[0];
        long fileSize = file.getSize();

        responseDTO.setStatus("SUCCESS");
        responseDTO.setMessage("굿즈 이미지 생성 완료");
        responseDTO.setGoodsImgUrl(goodsImgUrl);
        responseDTO.setGoodsImgSize(fileSize);
        responseDTO.setUploadImgGroupId(uploadImgGroup.getUploadGroupId());
        return ResponseEntity.ok(responseDTO);
    }

    /**
     * 굿즈 시안 3개 이미지 생성
     * @param user 생성한 사용자
     * @param dto 굿즈 옵션 값
     * @return 프론트에 나노바나나로 생성한 이미지 url
     */
    @PostMapping("/create-goods-sample")
    public ResponseEntity<CreateGoodsSampleResponseDTO> createGoodsSample(
            @AuthenticationPrincipal UserEntity user,
            @ModelAttribute CreateGoodsSampleRequestDTO dto
    ) {

        CreateGoodsSampleResponseDTO responseDTO = new CreateGoodsSampleResponseDTO();

        byte[] resultGoodsImageFileByte;

        try {
            resultGoodsImageFileByte = objectStorageService.downloadImage(dto.getResultImageUrl());
        } catch (IOException e) {
            log.error("이미지 다운로드 실패: {}", e.getMessage());
            responseDTO.setStatus("ERROR");
            responseDTO.setMessage("이미지 다운로드 실패: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
        }

        // byte[] -> MutilpartFile 로 변환
        MultipartFile resultGoodsImageFile = byteArrayToMultipartFile(resultGoodsImageFileByte);

        // 나노바나나를 통해 2개의 시안 이미지 생성
        List<MultipartFile> sampleGoodsImages = nanoService.createGoodsSampleImage(resultGoodsImageFileByte);

        List<String> sampleImgUrls = new ArrayList<>();
        
        if (resultGoodsImageFileByte != null && resultGoodsImageFileByte.length > 0) {
            // 폴더 경로 생성
            String folderPath = "sample" + "/" + user.getUserId() + "/" + dto.getUploadImgGroupId();

            try {
                String sampleImgUrl = objectStorageService.uploadFile(resultGoodsImageFile, user.getUserId(), folderPath);
                sampleImgUrls.add(sampleImgUrl);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                responseDTO.setStatus("ERROR");
                responseDTO.setMessage("시안 생성 실패: " + e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
            }

            for(int i = 0; i < sampleGoodsImages.size(); i++) {
                MultipartFile multipartFile = sampleGoodsImages.get(i);
                try {
                    String sampleImgUrl = objectStorageService.uploadFile(multipartFile, user.getUserId(), folderPath);
                    sampleImgUrls.add(sampleImgUrl);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    responseDTO.setStatus("ERROR");
                    responseDTO.setMessage("시안 생성 실패: " + e.getMessage());
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
                }
            }

            responseDTO.setStatus("SUCCESS");
            responseDTO.setMessage("시안 생성 완료");
            responseDTO.setGoodsSampleImgUrls(sampleImgUrls);
        } else {
            responseDTO.setStatus("ERROR");
            responseDTO.setMessage("이미지 파일 또는 URL이 없습니다.");
        }

        return ResponseEntity.ok(responseDTO);
    }

    /**
     * 뒤로가기시 ObjectStorage에 있는 sample 폴더 삭제하기
     * @param user
     * @param dto
     * @return
     */
    @PostMapping("/delete-goods-sample")
    public ResponseEntity<DeleteGoodsSampleResponseDTO> deleteSampleImg(
            @AuthenticationPrincipal UserEntity user,
            @RequestBody DeleteGoodsSampleRequestDTO dto
    ) {

        DeleteGoodsSampleResponseDTO response = goodsService.deleteSampleImg(dto);


        // 선택한 시안 db에 저장
        return ResponseEntity.ok(response);
    }


    /**
     * 시안 이미지 다운로드 (CORS 문제 해결을 위한 프록시)
     * @param imageUrl 다운로드할 이미지 URL (쿼리 파라미터)
     * @return 이미지 바이트 배열
     */
    @GetMapping("/download-image")
    public ResponseEntity<byte[]> downloadImage(
            @RequestParam("url") String imageUrl
    ) {
        try {
            byte[] imageBytes = objectStorageService.downloadImage(imageUrl);
            
            // Content-Type 설정 (이미지 타입 추출)
            String contentType = "image/jpeg"; // 기본값
            if (imageUrl.toLowerCase().endsWith(".png")) {
                contentType = "image/png";
            } else if (imageUrl.toLowerCase().endsWith(".gif")) {
                contentType = "image/gif";
            }
            
            // Content-Disposition 헤더 설정 (다운로드 파일명)
            String fileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
            if (fileName.isEmpty() || !fileName.contains(".")) {
                fileName = "image.jpg";
            }
            
            return ResponseEntity.ok()
                    .header("Content-Type", contentType)
                    .header("Content-Disposition", "attachment; filename=\"" + fileName + "\"")
                    .body(imageBytes);
        } catch (Exception e) {
            log.error("이미지 다운로드 실패: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 여러 이미지를 ZIP 파일로 다운로드
     * @param imageUrls 다운로드할 이미지 URL 리스트 (쿼리 파라미터)
     * @return ZIP 파일 바이트 배열
     */
    @GetMapping("/download-images-zip")
    public ResponseEntity<byte[]> downloadImagesAsZip(
            @RequestParam("urls") List<String> imageUrls
    ) {
        if (imageUrls == null || imageUrls.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ZipOutputStream zos = new ZipOutputStream(baos);

            for (int i = 0; i < imageUrls.size(); i++) {
                String imageUrl = imageUrls.get(i);
                try {
                    byte[] imageBytes = objectStorageService.downloadImage(imageUrl);
                    
                    // 파일명 추출
                    String fileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
                    if (fileName.isEmpty() || !fileName.contains(".")) {
                        // 확장자 추출 시도
                        String contentType = "image/jpeg";
                        if (imageUrl.toLowerCase().contains(".png")) {
                            contentType = "image/png";
                            fileName = "image_" + (i + 1) + ".png";
                        } else if (imageUrl.toLowerCase().contains(".gif")) {
                            contentType = "image/gif";
                            fileName = "image_" + (i + 1) + ".gif";
                        } else {
                            fileName = "image_" + (i + 1) + ".jpg";
                        }
                    }

                    // ZIP 엔트리 추가
                    ZipEntry entry = new ZipEntry(fileName);
                    entry.setSize(imageBytes.length);
                    zos.putNextEntry(entry);
                    zos.write(imageBytes);
                    zos.closeEntry();
                } catch (Exception e) {
                    log.warn("이미지 다운로드 실패 (URL: {}): {}", imageUrl, e.getMessage());
                    // 실패한 이미지는 건너뛰고 계속 진행
                }
            }

            zos.close();
            byte[] zipBytes = baos.toByteArray();

            return ResponseEntity.ok()
                    .header("Content-Type", "application/zip")
                    .header("Content-Disposition", "attachment; filename=\"goods_images.zip\"")
                    .body(zipBytes);
        } catch (Exception e) {
            log.error("ZIP 파일 생성 실패: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 시안 선택
     * @param user
     * @param dto
     * @return
     */
    @PostMapping("/select-goods")
    public ResponseEntity<SelectGoodsResponseDTO> selectGoods(
            @AuthenticationPrincipal UserEntity user,
            @RequestBody SelectGoodsRequestDTO dto
    ) {

        byte[] goodsImgFileByte;

        try {
            goodsImgFileByte = objectStorageService.downloadImage(dto.getGoodsImgUrl());
        } catch (IOException e) {
            log.error("이미지 다운로드 실패: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        MultipartFile goodsImgFile = byteArrayToMultipartFile(goodsImgFileByte);

        String goodsImgUrl = "";

        // 시안 선택한 이미지 url ObjectStorage에 저장
        try {
            goodsImgUrl = objectStorageService.uploadFile(goodsImgFile, user.getUserId(),
                    "goods" + "/" + user.getUserId() + "/" + dto.getUploadImgGroupId()
            );
        } catch (IOException e) {
            log.error("선택한 이미지 ObjectStorage 저장 실패: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        // 결과 이미지 Object Storage 삭제
        objectStorageService.deleteImage(dto.getResultImageUrl());

        // 시안 3개 이미지 Object Storage 삭제
        List<String> sampleGoodsImageUrls = dto.getSampleGoodsImageUrl();

        for (String url : sampleGoodsImageUrls) {
            objectStorageService.deleteImage(url);
        }

        // 선택한 시안 db에 저장
        return ResponseEntity.ok(goodsService.selectGoods(user, dto, goodsImgUrl, goodsImgFile));
    }

    /**
     * 내가 생성한 굿즈 리스트 불러오기
     */
    @GetMapping("/select-all-my-goods")
    public ResponseEntity<List<SelectAllMyGoodsResponseDTO>> selectAllMyGoods(
            @AuthenticationPrincipal UserEntity user
    ) {
        return ResponseEntity.ok(goodsService.selectAllMyGoods(user));
    }

    // byte[] -> MultipartFile 로 변환
    private MultipartFile byteArrayToMultipartFile(byte[] byteArray) {
        return new MultipartFile() {
            @Override
            public String getName() {
                return "result-image";
            }
            @Override
            public String getOriginalFilename() {
                return "result-image.jpg";
            }
            @Override
            public String getContentType() {
                return "image/jpeg";
            }
            @Override
            public boolean isEmpty() {
                return byteArray == null || byteArray.length == 0;
            }
            @Override
            public long getSize() {
                return byteArray != null ? byteArray.length : 0;
            }
            @Override
            public byte[] getBytes() throws IOException {
                return byteArray;
            }
            @Override
            public java.io.InputStream getInputStream() throws IOException {
                return new java.io.ByteArrayInputStream(byteArray);
            }
            @Override
            public void transferTo(java.io.File dest) throws IOException, IllegalStateException {
                throw new UnsupportedOperationException("Not implemented");
            }
        };
    }

    /**
     * 굿즈 둘러보기
     * @param categoryId
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/browse")
    public List<GoodsBrowseDTO> browseGoods(
            @RequestParam(name = "categoryId", required = false) Long categoryId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "8") int size) {

        
        if (categoryId != null && categoryId == 0) {
            categoryId = null;
        }

        Pageable pageable = PageRequest.of(page, size);
        return goodsService.browseGoods(categoryId, pageable);
    }

    /**
     * 굿즈 상세 조회 (조회수 1증가)
     */
    @PostMapping("/view-goods")
    public ResponseEntity<GoodsDetailDTO> viewGoods(
            @RequestBody GoodsDetailRequestDTO dto,
            @AuthenticationPrincipal UserEntity user) {

        GoodsDetailDTO response = goodsService.viewGoods(dto.getGoodsId(), user);

        return ResponseEntity.ok(response);
    }

}
