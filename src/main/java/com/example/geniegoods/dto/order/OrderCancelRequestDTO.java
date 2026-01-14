package com.example.geniegoods.dto.order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Schema(description = "주문 취소 요청")
public class OrderCancelRequestDTO {
    @Schema(description = "주문 PK", example = "1")
    private Long orderId;
}
