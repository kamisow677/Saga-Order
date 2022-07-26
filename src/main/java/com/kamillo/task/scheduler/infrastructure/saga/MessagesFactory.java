package com.kamillo.task.scheduler.infrastructure.saga;

import com.kamillo.task.scheduler.domain.OrderDomain;
import com.kamillo.task.scheduler.domain.saga.MessagesSender;
import com.kamillo.task.scheduler.domain.saga.SagaSeatEvents;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

@Configuration
public class MessagesFactory implements MessagesSender<SagaSeatEvents> {

    public static final String ORDER_ID_HEADER = "orderId";

    @Override
    public Message create(SagaSeatEvents event, OrderDomain order) {
        return MessageBuilder
                .withPayload(event)
                .setHeader(ORDER_ID_HEADER, order.getOrderId())
                .build();
    }
}
