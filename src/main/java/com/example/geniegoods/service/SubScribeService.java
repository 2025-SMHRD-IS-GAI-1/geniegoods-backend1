package com.example.geniegoods.service;

import com.example.geniegoods.dto.subscribe.ChangeSubScribeRequestDTO;
import com.example.geniegoods.dto.subscribe.ChangeSubScribeResponseDTO;
import com.example.geniegoods.entity.SubScribeEntity;
import com.example.geniegoods.entity.UserEntity;
import com.example.geniegoods.enums.PaymentMethod;
import com.example.geniegoods.repository.SubScribeRepository;
import com.example.geniegoods.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SubScribeService {

    private final UserRepository userRepository;

    private final SubScribeRepository subScribeRepository;

    /**
     * 구독 플랜 변경
     * @param currentUser
     * @param request
     * @return
     */
    public ChangeSubScribeResponseDTO changePlan(UserEntity currentUser, ChangeSubScribeRequestDTO request) {

        String subscriptionPlan = request.getSubscriptionPlan();

        // 현재 플랜과 같은 플랜으로 변경할 경우 막음
        if(currentUser.getSubscriptionPlan().equals(subscriptionPlan)) {
            throw new IllegalArgumentException("현재 플랜과 같습니다.");
        }

        // 현재 유저 subscriptionPlan 변경
        currentUser.setSubscriptionPlan(subscriptionPlan);

        userRepository.save(currentUser);

        // 플랜 FREE 가 아닐때만 DB insert
        if(!subscriptionPlan.equals("FREE")) {
            subScribeRepository.save(SubScribeEntity.builder()
                    .method(PaymentMethod.from(request.getMethod()))
                    .planName(subscriptionPlan)
                    .price(9900)
                    .user(currentUser)
                    .build());
        }

        return ChangeSubScribeResponseDTO.builder()
                .subscriptionPlan(request.getSubscriptionPlan())
                .build();
    }
}
