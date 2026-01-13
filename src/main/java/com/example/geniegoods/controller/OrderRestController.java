package com.example.geniegoods.controller;

import com.example.geniegoods.dto.order.*;
import com.example.geniegoods.entity.OrderEntity;
import com.example.geniegoods.entity.UserEntity;
import com.example.geniegoods.enums.OrderStatus;
import com.example.geniegoods.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Tag(name = "Order API", description = "주문 관련 API")
@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderRestController {
	
    private final OrderService orderService;
    private static final int PAGE_SIZE = 3; // 페이지 수

    @Operation(summary = "주문 생성", description = "주문 생성")
    @PostMapping("/create")
    public ResponseEntity<String> createOrder(@AuthenticationPrincipal UserEntity user, @RequestBody OrderRequestDTO dto) {

        orderService.createOrder(user, dto);

        return ResponseEntity.ok("주문 생성 성공");
    }

    @Operation(summary = "최근 주문내역 2건 조회", description = "최근 주문내역 2건 조회")
    @GetMapping("recent-order")
    public ResponseEntity<List<RecentOrderResponseDTO>> selectRecentOrder(@AuthenticationPrincipal UserEntity user) {

        List<RecentOrderResponseDTO> response = orderService.selectRecentOrder(user);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "주문 목록 조회", description = "페이징 + 기간 필터 적용")
    @GetMapping("all-orders")
    public ResponseEntity<Map<String, Object>> getMyOrders(
            @AuthenticationPrincipal UserEntity user,
            @RequestParam(name = "months", required = false) Integer months,
            @RequestParam(name = "page", defaultValue = "0") int page) {

        if (user == null) {
            return ResponseEntity.status(401).build();
        }

        List<AllOrderResponseDTO> orders = orderService.getMyOrdersManualPaging(user.getUserId(), months, page);

        int totalElements = orderService.getTotalOrderCount(user.getUserId(), months);
        int totalPages = (int) Math.ceil((double) totalElements / PAGE_SIZE);

        Map<String, Object> response = new HashMap<>();
        response.put("content", orders);
        response.put("totalElements", totalElements);
        response.put("totalPages", totalPages);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "주문 상세 조회", description = "주문 상세 조회")
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponseDTO> getOrderDetail(
            @PathVariable("orderId") Long orderId,
            @AuthenticationPrincipal UserEntity user) {

        if (user == null) {
            return ResponseEntity.status(401).build();
        }

        // 여기서 getOrderDetail 호출 (인자 2개!)
        OrderResponseDTO order = orderService.getOrderDetail(orderId, user.getUserId());
        return ResponseEntity.ok(order);
    }

    @Operation(summary = "주문 취소", description = "주문 취소")
    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<ApiResponseDTO> cancelOrder(
            @PathVariable(name = "orderId") Long orderId,
            @AuthenticationPrincipal UserEntity user) {

        if (user == null) {
            return ResponseEntity.status(401).body(new ApiResponseDTO("로그인 후 이용해주세요."));
        }

        OrderEntity order = orderService.findByOrderIdAndUserId(orderId, user.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("주문을 찾을 수 없거나 권한이 없습니다."));

        // 이미 취소됐거나 배송 시작됐으면 불가
        if (order.getStatus() == OrderStatus.CANCELED) {
            return ResponseEntity.badRequest().body(new ApiResponseDTO("이미 취소된 주문입니다."));
        }
        if (order.getStatus() == OrderStatus.DELIVERING || order.getStatus() == OrderStatus.DELIVERED) {
            return ResponseEntity.badRequest().body(new ApiResponseDTO("배송이 시작된 주문은 취소할 수 없습니다."));
        }

        // 취소 처리
        order.setStatus(OrderStatus.CANCELED);
        // orderService.cancelOrder(orderId, user);

        return ResponseEntity.ok(new ApiResponseDTO("주문이 취소되었습니다. 환불 처리될 예정입니다."));
    }

    @Operation(summary = "주문 주소 수정", description = "주문 주소 수정")
    @PutMapping("/{orderId}/address")
    public ResponseEntity<String> updateOrderAddress(
            @PathVariable("orderId") Long orderId,
            @RequestBody OrderAddressRequestDTO addressRequest) {
        orderService.updateOrderAddress(orderId, addressRequest);
        return ResponseEntity.ok("주문 주소 수정 성공");
    }
    
    
}