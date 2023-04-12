package com.kamillo.task.scheduler.infrastructure.saga;

import com.kamillo.task.scheduler.order.domain.OrderDomain;
import com.kamillo.task.scheduler.domain.saga.MessagesSender;
import com.kamillo.task.scheduler.domain.saga.SagaSeatEvents;
import com.kamillo.task.scheduler.infrastructure.api.BlockSeatParams;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

@Configuration
public class MessagesFactory implements MessagesSender<SagaSeatEvents> {

    public static final String ORDER_ID_HEADER = "orderId";
    public static final String SEATS_PARAMS = "seats";
    public static final String ORDER = "order";

    @Override
    public Message<SagaSeatEvents> create(SagaSeatEvents event, OrderDomain order, BlockSeatParams blockSeatParams) {
        return MessageBuilder
                .withPayload(event)
                .setHeader(ORDER_ID_HEADER, order.getOrderId())
                .setHeader(ORDER, order)
                .setHeader(SEATS_PARAMS, blockSeatParams)
                .build();
    }

    @Override
    public Message<SagaSeatEvents> create(SagaSeatEvents event, OrderDomain order) {
        return MessageBuilder
                .withPayload(event)
                .setHeader(ORDER_ID_HEADER, order.getOrderId())
                .build();
    }
}
