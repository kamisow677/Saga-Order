package com.kamillo.task.scheduler.domain.saga;

import com.kamillo.task.scheduler.domain.OrderDomain;
import org.springframework.messaging.Message;

public interface MessagesSender <T> {
    public Message<T> create(T event, OrderDomain order);
}
