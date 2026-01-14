// com.example.geniegoods.dto.order.OrderRequestDto.java

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
@Schema(description = "주문 생성 요청")
public class OrderRequestDTO {

    @Schema(description = "각각 굿즈들")
    private List<OrderItemDto> items;

    @Schema(description = "우편코드", example = "06035")
    private String zipcode;

    @Schema(description = "주소", example = "서울 강남구 가로수길 9 (신사동)")
    private String address;

    @Schema(description = "상세주소", example = "103호")
    private String detailAddress;

    @Schema(description = "결제 방법", example = "TOSS PAY")
    private String method;

    // 내부 클래스도 어노테이션 제거
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @ToString
    public static class OrderItemDto {
        @Schema(description = "굿즈 PK", example = "1")
        private Long goodsId;   // 굿즈 ID
        @Schema(description = "굿즈 수량", example = "3")
        private Integer quantity;   // 수량
    }
}