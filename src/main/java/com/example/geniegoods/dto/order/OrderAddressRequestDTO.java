// com.example.geniegoods.dto.order.OrderRequestDto.java

package com.example.geniegoods.dto.order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Schema(description = "주문 상세 주소 변경 요청")
public class OrderAddressRequestDTO {

    @Schema(description = "주소", example = "서울 강남구 가로수길 9 (신사동)")
    private String zipcode;

    @Schema(description = "상세주소", example = "103호")
    private String address;

    @Schema(description = "결제 방법", example = "TOSS PAY")
    private String detailAddress;
}