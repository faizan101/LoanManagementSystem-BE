package com.dtone.lending;

import com.dtone.lending.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.util.concurrent.BlockingQueue;

/**
 * @author muhammad.faizan
 * @version 1.0
 * @description This class is responsible for initializing all the schedulers and scheduling them based on the
 * cron expression.
 */
@Slf4j
@Component
public class ScheduledTasks {

    @Autowired
    @Qualifier("AsyncThreadPool")
    private TaskExecutor taskExecutor;

    @Autowired
    private PaymentService paymentService;

    @Scheduled(cron = "${app.billing.fixedDelayMilliseconds}")
    public void loanPaymentsJobNotification() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        ThreadPoolTaskExecutor tpe = (ThreadPoolTaskExecutor) taskExecutor;
        BlockingQueue<Runnable> queue = tpe.getThreadPoolExecutor().getQueue();
        log.info("Thread Pool Size : " + tpe.getCorePoolSize() + " Thread Max Pool Size : " + tpe.getMaxPoolSize());
        log.info("Queue Size : " + queue.size() + " Queue Remaining Size : " + queue.remainingCapacity());
        float percent = ((float) (queue.size() * 100) / (float) (queue.size() + queue.remainingCapacity()));
        if (percent < 50) {
            log.info("Queue Size below threshold adding new records");
            paymentService.postLoanClaimsEvents();
        }
        stopWatch.stop();
        log.info("{} data end / {}", stopWatch.getTotalTimeMillis());
    }
}