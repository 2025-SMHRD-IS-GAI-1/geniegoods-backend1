package com.example.geniegoods.controller;

import com.example.geniegoods.dto.common.CommonResponseDTO;
import com.example.geniegoods.dto.order.*;
import com.example.geniegoods.entity.UserEntity;
import com.example.geniegoods.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Tag(name = "Order API", description = "주문 관련 API")
@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderRestController {
	
    private final OrderService orderService;
    private static final int PAGE_SIZE = 3; // 페이지 수

    @Operation(summary = "주문 생성", description = "주문 생성")
    @PostMapping("/create")
    public ResponseEntity<CommonResponseDTO> createOrder(@AuthenticationPrincipal UserEntity user, @RequestBody OrderRequestDTO dto) {

        orderService.createOrder(user, dto);

        return ResponseEntity.ok(
                CommonResponseDTO.builder()
                        .message("주문 생성 완료")
                        .build()
        );
    }

    @Operation(summary = "최근 주문내역 2건 조회", description = "최근 주문내역 2건 조회")
    @GetMapping("recent-order")
    public ResponseEntity<List<RecentOrderResponseDTO>> selectRecentOrder(@AuthenticationPrincipal UserEntity user) {

        List<RecentOrderResponseDTO> response = orderService.selectRecentOrder(user);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "주문 목록 조회", description = "페이징 + 기간 필터 적용")
    @GetMapping("all-orders")
    public ResponseEntity<MyOrderResponseDTO> getMyOrders(
            @AuthenticationPrincipal UserEntity user,
            @Schema(description = "월", example = "3")
            @RequestParam(name = "months", required = false) Integer months,
            @Schema(description = "페이지", example = "0")
            @RequestParam(name = "page", defaultValue = "0") int page) {

        return ResponseEntity.ok(orderService.getMyOrdersManualPaging(user.getUserId(), months, page));
    }

    @Operation(summary = "주문 상세 조회", description = "주문 상세 조회")
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponseDTO> getOrderDetail(
            @Schema(description = "주문 PK", example = "1")
            @PathVariable("orderId") Long orderId,
            @AuthenticationPrincipal UserEntity user) {

        return ResponseEntity.ok(orderService.getOrderDetail(orderId, user.getUserId()));
    }

    @Operation(summary = "주문 취소", description = "주문 취소")
    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<CommonResponseDTO> cancelOrder(
            @Schema(description = "주문 PK", example = "1")
            @PathVariable(name = "orderId") Long orderId,
            @AuthenticationPrincipal UserEntity user) {

        orderService.cancelOrder(orderId, user.getUserId());


        return ResponseEntity.ok(CommonResponseDTO.builder()
                .message("주문이 취소되었습니다. 환불 처리될 예정입니다.")
                .build());
    }

    @Operation(summary = "주문 주소 수정", description = "주문 주소 수정")
    @PutMapping("/{orderId}/address")
    public ResponseEntity<CommonResponseDTO> updateOrderAddress(
            @Schema(description = "주문 PK", example = "1")
            @PathVariable("orderId") Long orderId,
            @RequestBody OrderAddressRequestDTO addressRequest) {

        orderService.updateOrderAddress(orderId, addressRequest);

        return ResponseEntity.ok(CommonResponseDTO.builder()
                .message("주문 주소 수정 성공")
                .build());
    }

}