package com.kamillo.task.scheduler.domain.order;

import com.kamillo.task.scheduler.domain.OrderDomain;
import com.kamillo.task.scheduler.domain.saga.SagaSeatEnum;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository {

    Optional<OrderDomain> getOrder(UUID orderId);

    OrderDomain updateOrderState(UUID orderId, SagaSeatEnum state);

    void saveOrUpdateOrderState(UUID orderId, SagaSeatEnum state);
}
