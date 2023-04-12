package com.kamillo.task.scheduler.order.domain;

import com.kamillo.task.scheduler.order.infra.NoSuchOrderException;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class OrderFactory {
    private final OrderRepository repository;

    public OrderFactory(OrderRepository repository) {
        this.repository = repository;
    }

    public OrderDomain getOrder(UUID orderId) {
        return repository.getOrder(orderId)
                .orElseThrow(() -> new NoSuchOrderException(orderId));
    }

}
