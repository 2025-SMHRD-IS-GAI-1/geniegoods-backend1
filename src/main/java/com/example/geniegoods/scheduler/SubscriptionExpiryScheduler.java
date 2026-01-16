package com.example.geniegoods.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.parameters.JobParameters;
import org.springframework.batch.core.job.parameters.JobParametersBuilder;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class SubscriptionExpiryScheduler {

    private final Job expireSubscriptionJob;
    private final JobOperator jobOperator;

    /**
     * 매일 오전 9시에 구독 만료 처리 Job 실행
     * cron 표현식: 초 분 시 일 월 요일
     * 0 0 9 * * ? = 매일 9시 0분 0초
     */
    @Scheduled(cron = "0 0 9 * * ?")
    public void expireSubscriptions() {
        log.info("구독 만료 처리 스케줄러 실행 시작: {}", LocalDateTime.now());

        try {
            // Job 실행을 위한 고유한 파라미터 생성 (같은 파라미터로는 Job이 재실행되지 않음)
            JobParameters jobParameters = new JobParametersBuilder()
                    .addString("executionTime", LocalDateTime.now().toString())
                    .toJobParameters();

            jobOperator.start(expireSubscriptionJob, jobParameters);
            log.info("구독 만료 처리 스케줄러 실행 완료: {}", LocalDateTime.now());
        } catch (Exception e) {
            log.error("구독 만료 처리 스케줄러 실행 중 오류 발생", e);
        }
    }
}
