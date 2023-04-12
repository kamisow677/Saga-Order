package com.kamillo.task.scheduler.infrastructure.task;


import com.kamillo.task.scheduler.domain.scheduler.SchedulerServiceFacade;
import com.kamillo.task.scheduler.domain.scheduler.TaskFactory;
import com.kamillo.task.scheduler.order.infra.GeneratedOrderRepo;
import com.kamillo.task.scheduler.order.infra.NoSuchOrderException;
import com.kamillo.task.scheduler.order.infra.PostgresOrder;
import jakarta.annotation.PreDestroy;
import jakarta.transaction.Transactional;
import org.quartz.TriggerKey;
import org.springframework.stereotype.Component;


import java.time.OffsetTime;
import java.util.UUID;

@Component
public class MyTaskFactory implements TaskFactory {

    private final GeneratedOrderRepo orderRepository;
    private final GeneratedTaskRepo generatedTaskRepo;
    private final SchedulerServiceFacade schedulerServiceFacade;
    private final TriggerKey triggerKey;

    public MyTaskFactory(GeneratedOrderRepo orderRepository, GeneratedTaskRepo generatedTaskRepo, SchedulerServiceFacade schedulerServiceFacade) {
        this.orderRepository = orderRepository;
        this.generatedTaskRepo = generatedTaskRepo;
        this.schedulerServiceFacade = schedulerServiceFacade;
        this.triggerKey = setup();
    }

    @Override
    @Transactional
    public Task createTask(UUID orderId, TaskType type) {
        PostgresOrder order = orderRepository.getByOrderID(orderId)
                .orElseThrow(() -> new NoSuchOrderException(orderId));
        Task task = new Task.TaskBuilder()
                .type(type)
                .status(TaskStatus.PENDING)
                .createdAt(OffsetTime.now())
                .order(order).build();
        return generatedTaskRepo.save(task);
    }

    public TriggerKey setup() {
        return schedulerServiceFacade.schedulePaymentFinishedJob();
    }

    @PreDestroy
    public void clean() {
        schedulerServiceFacade.unschedulePaymentFinishedJob(triggerKey);
    }

}
