package com.kamillo.task.scheduler.order.infra;

import com.kamillo.task.scheduler.order.domain.OrderDomain;
import org.springframework.stereotype.Component;

@Component
class OrderMapper {

    public PostgresOrder toOrder(OrderDomain domain) {
        return PostgresOrder.builder()
                .orderId(domain.getOrderId())
                .orderState(domain.getOrderState())
                .build();
    }

    public OrderDomain toOrderDomain(PostgresOrder postgresOrder) {
        return OrderDomain.builder()
                .orderId(postgresOrder.getOrderId())
                .orderState(postgresOrder.getOrderState())
                .build();
    }

}
