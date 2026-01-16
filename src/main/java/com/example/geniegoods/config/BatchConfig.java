package com.example.geniegoods.config;

import com.example.geniegoods.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.infrastructure.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class BatchConfig {

    private final UserService userService;
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    /**
     * 구독 만료 처리 Tasklet
     */
    @Bean
    public Tasklet expireSubscriptionTasklet() {
        return (contribution, chunkContext) -> {
            log.info("구독 만료 처리 작업 시작");
            try {
                int expiredCount = userService.expireSubscriptions();
                log.info("구독 만료 처리 완료: {}명의 사용자가 FREE 플랜으로 변경되었습니다.", expiredCount);
                return RepeatStatus.FINISHED;
            } catch (Exception e) {
                log.error("구독 만료 처리 중 오류 발생", e);
                throw e;
            }
        };
    }

    /**
     * 구독 만료 처리 Step
     */
    @Bean
    public Step expireSubscriptionStep() {
        return new StepBuilder("expireSubscriptionStep", jobRepository)
                .tasklet(expireSubscriptionTasklet(), transactionManager)
                .build();
    }

    /**
     * 구독 만료 처리 Job
     */
    @Bean
    public Job expireSubscriptionJob() {
        return new JobBuilder("expireSubscriptionJob", jobRepository)
                .start(expireSubscriptionStep())
                .build();
    }
}
