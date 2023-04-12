package com.kamillo.task.scheduler.domain.scheduler;

import com.kamillo.task.scheduler.infrastructure.task.GeneratedTaskRepo;
import com.kamillo.task.scheduler.infrastructure.task.Task;
import com.kamillo.task.scheduler.infrastructure.task.TaskStatus;
import com.kamillo.task.scheduler.infrastructure.task.TaskType;
import org.quartz.JobExecutionContext;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.time.OffsetTime;
import java.time.temporal.ChronoUnit;
import java.util.List;


@Component
public class PaymentFinishedJob extends QuartzJobBean {

    private final PaymentFinishedJobProperties properties;

    private GeneratedTaskRepo generatedTaskRepo;

    public PaymentFinishedJob(PaymentFinishedJobProperties properties, GeneratedTaskRepo generatedTaskRepo) {
        this.properties = properties;
        this.generatedTaskRepo = generatedTaskRepo;
    }

    public void executeInternal(JobExecutionContext jobContext) {
        List<Task> allByType = generatedTaskRepo.findAllByType(TaskType.CHECK_PAYMENT_FINISHED)
                .stream()
                .filter((Task task) -> task.getStatus().equals(TaskStatus.PENDING))
                .toList();
        for (Task task : allByType) {
            switch (task.getOrder().getOrderState()) {
                case SEAT_PAYMENT_PENDING -> {
                    if (task.getCreatedAt().plusSeconds(properties.timeToFinish).isBefore(OffsetTime.now())) {
                        System.out.println("TaskId: " + task.getId() + " payment pending failed for order: " + task.getOrder().getOrderId());
                        task.setStatus(TaskStatus.FAILED);
                    } else {
                        System.out.println("TaskId: " + task.getId() + " in status: " + task.getOrder().getOrderState() + " has " +
                                ChronoUnit.SECONDS.between(OffsetTime.now(), task.getCreatedAt().plusSeconds(properties.timeToFinish)) +" seconds to finish");
                    }
                    generatedTaskRepo.save(task);
                }
                case SEAT_PAYMENT_REJECTED, SEAT_ALLOCATED -> {
                    System.out.println("TaskId: " + task.getId() + " in status: " + task.getOrder().getOrderState() +  " payment done for order: " + task.getOrder().getOrderId());
                    task.setStatus(TaskStatus.DONE);
                    generatedTaskRepo.save(task);
                }
            }
        }
    }
}