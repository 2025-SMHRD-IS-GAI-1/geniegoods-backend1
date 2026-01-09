package com.example.geniegoods.service;

import com.example.geniegoods.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j  // log 사용을 위해 추가 (필요시)
public class PaymentService {

    private final PaymentRepository paymentRepository;
}
