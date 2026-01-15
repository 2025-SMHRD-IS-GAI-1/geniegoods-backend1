package com.example.geniegoods.controller;

import com.example.geniegoods.dto.subscribe.ChangeSubScribeRequestDTO;
import com.example.geniegoods.dto.subscribe.ChangeSubScribeResponseDTO;
import com.example.geniegoods.entity.UserEntity;
import com.example.geniegoods.service.SubScribeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "SubScribe API", description = "구독 관련 API")
@RestController
@RequestMapping("/api/subscribe")
@RequiredArgsConstructor
public class SubScribeRestController {

    private final SubScribeService subScribeService;

    @Operation(summary = "구독 플랜 변경", description = "구독 플랜 변경")
    @PostMapping("/change-plan")
    public ResponseEntity<ChangeSubScribeResponseDTO> changePlan(
            @AuthenticationPrincipal UserEntity currentUser,
            @RequestBody ChangeSubScribeRequestDTO request) {

        return ResponseEntity.ok(subScribeService.changePlan(currentUser, request));
    }

}
