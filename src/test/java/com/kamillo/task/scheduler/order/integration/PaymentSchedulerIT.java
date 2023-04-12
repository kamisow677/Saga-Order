package com.kamillo.task.scheduler.order.integration;

import com.kamillo.task.scheduler.domain.saga.SagaSeatEnum;
import com.kamillo.task.scheduler.order.infra.GeneratedOrderRepo;
import com.kamillo.task.scheduler.order.infra.PostgresOrder;
import com.kamillo.task.scheduler.infrastructure.task.GeneratedTaskRepo;
import com.kamillo.task.scheduler.infrastructure.task.Task;
import com.kamillo.task.scheduler.infrastructure.task.TaskStatus;
import com.kamillo.task.scheduler.infrastructure.task.TaskType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Duration;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;

public class PaymentSchedulerIT extends BaseIT {

    @Autowired
    private GeneratedOrderRepo generatedOrderRepo;

    @Autowired
    private GeneratedTaskRepo generatedTaskRepo;

    @AfterEach
    public void clean() {
        generatedOrderRepo.deleteAll();
        generatedTaskRepo.deleteAll();
    }

    @Test
    public void shouldSetOrderFailedIfNoPaymentWasDone() {
        // given
        UUID orderId = UUID.randomUUID();
        generatedOrderRepo.save(PostgresOrder.builder().orderId(orderId).orderState(SagaSeatEnum.SEAT_BLOCKED).build());

        // when
        post("/public/start_payment",  String.class, Map.of(ORDER_ID_PARAM, orderId.toString()), null);

        // then
        await()
                .atLeast(Duration.ZERO)
                .atMost(Duration.ofMinutes(1))
                .with()
                .pollInterval(Duration.ofSeconds(2))
                .untilAsserted(() -> {
                    assertEquals(TaskStatus.FAILED, (getTask(orderId).isPresent()) ? getTask(orderId).get().getStatus() : null);
                });
    }

    @Test
    public void shouldSetOrderDoneIfPaymentWasDone() throws InterruptedException {
        // given
        UUID orderId = UUID.randomUUID();
        generatedOrderRepo.save(PostgresOrder.builder().orderId(orderId).orderState(SagaSeatEnum.SEAT_BLOCKED).build());

        // when
        post("/public/start_payment",  String.class, Map.of(ORDER_ID_PARAM, orderId.toString()), null);
        sleep(2);
        post("/public/accepted_payment",  String.class, Map.of(ORDER_ID_PARAM, orderId.toString()), null);

        // then
        await()
                .atLeast(Duration.ZERO)
                .atMost(Duration.ofMinutes(1))
                .with()
                .pollInterval(Duration.ofSeconds(2))
                .untilAsserted(() -> {
                    assertEquals(TaskStatus.DONE, (getTask(orderId).isPresent()) ? getTask(orderId).get().getStatus() : null);
                });
    }

    private void sleep(long seconds) throws InterruptedException {
        Thread.sleep(seconds * 1000);
    }

    private Optional<Task> getTask(UUID orderId) {
        return generatedTaskRepo.findAll()
                .stream()
                .filter(x -> x.getType().equals(TaskType.CHECK_PAYMENT_FINISHED) && x.getOrder().getOrderId().equals(orderId))
                .findFirst();
    }

}