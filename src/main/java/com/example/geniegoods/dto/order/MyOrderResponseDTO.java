package com.example.geniegoods.dto.order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Schema(description = "주문 목록 응답")
public class MyOrderResponseDTO {

    @Schema(description = "주문 목록")
    private List<AllOrderResponseDTO> contents;

    @Schema(description = "주문 목록 전체 개수", example = "23")
    private int totalElements;

    @Schema(description = "페이지 계산", example = "4")
    private int totalPages;
}
