package com.kamillo.task.scheduler.domain.saga;

import com.kamillo.task.scheduler.domain.OrderDomain;
import com.kamillo.task.scheduler.infrastructure.api.BlockSeatParams;
import org.springframework.messaging.Message;

public interface MessagesSender <T> {
    Message<T> create(T event, OrderDomain order, BlockSeatParams blockSeatParams);

    Message<T> create(T event, OrderDomain order);
}
