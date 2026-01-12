package com.example.geniegoods.dto.goods;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class NanobananaComposeResponseDTO {
    private String message;
    private String output_path;
    private Boolean saved;
    private String result_data;  // base64 인코딩된 이미지 데이터
}

