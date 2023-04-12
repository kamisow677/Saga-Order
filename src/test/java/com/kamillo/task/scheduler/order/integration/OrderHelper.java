package com.kamillo.task.scheduler.order.integration;

import com.kamillo.task.scheduler.domain.saga.SagaSeatEnum;
import com.kamillo.task.scheduler.order.infra.GeneratedSeatsRepo;
import com.kamillo.task.scheduler.order.infra.PostgresSeats;
import com.kamillo.task.scheduler.infrastructure.task.GeneratedTaskRepo;
import com.kamillo.task.scheduler.order.infra.GeneratedOrderRepo;
import com.kamillo.task.scheduler.order.infra.PostgresOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public class OrderHelper {

    @Autowired
    private GeneratedOrderRepo orderRepo;

    @Autowired
    private GeneratedTaskRepo taskRepo;

    @Autowired
    private GeneratedSeatsRepo seatsRepo;

    @Transactional
    public PostgresOrder addAnySeatToOrder(UUID orderId) {
        PostgresSeats postgresSeats = seatsRepo.findAll().get(0);
        postgresSeats.setFree(false);
        PostgresOrder order = PostgresOrder.builder()
                .orderId(orderId)
                .orderState(SagaSeatEnum.SEAT_PAYMENT_PENDING)
                .build();
        order.setSeats(postgresSeats);
       return orderRepo.save(order);

    }

}
