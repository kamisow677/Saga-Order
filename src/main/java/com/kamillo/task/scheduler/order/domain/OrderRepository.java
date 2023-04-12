package com.kamillo.task.scheduler.order.domain;

import com.kamillo.task.scheduler.domain.saga.SagaSeatEnum;
import com.kamillo.task.scheduler.infrastructure.api.BlockSeatParams;

import java.util.Optional;
import java.util.UUID;

public interface OrderRepository {

    Optional<OrderDomain> getOrder(UUID orderId);

    OrderDomain updateOrderState(UUID orderId, SagaSeatEnum state);

    void saveOrUpdateOrderState(UUID orderId, SagaSeatEnum state);

    OrderDomain saveOrder(OrderDomain domain);

    OrderDomain createOrderWithBlockedSeats(OrderDomain domain, BlockSeatParams blockSeatParams);

    boolean decoupleSeats(UUID orderId);
}
