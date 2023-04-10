package com.kamillo.task.scheduler.order.domain;

import com.kamillo.task.scheduler.domain.SeatsDomain;
import com.kamillo.task.scheduler.domain.saga.SagaSeatEnum;
import com.kamillo.task.scheduler.infrastructure.api.BlockSeatParams;
import com.kamillo.task.scheduler.order.infra.NoSuchOrderException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;

import java.util.UUID;

import static com.kamillo.task.scheduler.domain.saga.SagaSeatEnum.SEAT_FREE;

@Data
@Builder
@AllArgsConstructor
public class OrderDomain {
    private final UUID orderId;
    private final SagaSeatEnum orderState;
    private final SeatsDomain seatsDomain;
    private final Integer version;

    public OrderDomain(UUID orderId, SagaSeatEnum orderState) {
        this.orderId = orderId;
        this.orderState = orderState;
        this.seatsDomain = null;
        version = null;
    }

    public OrderDomain(UUID orderId) {
        this.orderId = orderId;
        this.orderState = SEAT_FREE;
        this.seatsDomain = null;
        version = null;
    }

}
