package com.example.geniegoods.dto.goods;

import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class YoloDetectionResponseDTO {
    private Integer total_detections;
    private Integer kept_detections;
    private List<CropInfoDTO> crops;
    private String preview_path;
    
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @ToString
    public static class CropInfoDTO {
        private Integer global_idx;
        private String crop_id;
        private String src_img;
        private String label;
        private Double conf;
        private String crop_path;
        private String crop_data;  // base64 인코딩된 이미지 데이터
    }
}

