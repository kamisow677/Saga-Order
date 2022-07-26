package com.kamillo.task.scheduler.domain;

import com.kamillo.task.scheduler.domain.saga.SagaSeatEnum;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

import static com.kamillo.task.scheduler.domain.saga.SagaSeatEnum.SEAT_FREE;

@Data
@Builder
public class OrderDomain {
    private final UUID orderId;
    private final SagaSeatEnum orderState;

    public OrderDomain(UUID orderId, SagaSeatEnum orderState) {
        this.orderId = orderId;
        this.orderState = orderState;
    }

    public OrderDomain(UUID orderId) {
        this.orderId = orderId;
        this.orderState = SEAT_FREE;
    }
}
