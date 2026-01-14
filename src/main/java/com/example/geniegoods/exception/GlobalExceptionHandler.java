package com.example.geniegoods.exception;

import com.example.geniegoods.dto.common.CommonResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 잘못된 인자나 파라미터 예외 처리
     * HTTP 400 Bad Request 반환
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<CommonResponseDTO> handleIllegalArgumentException(IllegalArgumentException e) {
        log.warn("IllegalArgumentException 발생: {}", e.getMessage());
        return ResponseEntity.badRequest()
                .body(CommonResponseDTO.builder()
                        .message(e.getMessage())
                        .build());
    }

    /**
     * 잘못된 상태 예외 처리 (권한 없음 등)
     * HTTP 403 Forbidden 반환
     */
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<CommonResponseDTO> handleIllegalStateException(IllegalStateException e) {
        log.warn("IllegalStateException 발생: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(CommonResponseDTO.builder()
                        .message(e.getMessage())
                        .build());
    }

    /**
     * 기타 예상치 못한 예외 처리
     * HTTP 500 Internal Server Error 반환
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<CommonResponseDTO> handleException(Exception e) {
        log.error("예상치 못한 예외 발생", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(CommonResponseDTO.builder()
                        .message("서버 오류가 발생했습니다.")
                        .build());
    }
}

