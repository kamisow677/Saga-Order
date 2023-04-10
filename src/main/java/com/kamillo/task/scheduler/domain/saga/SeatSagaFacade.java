package com.kamillo.task.scheduler.domain.saga;

import com.kamillo.task.scheduler.order.domain.OrderDomain;
import com.kamillo.task.scheduler.order.domain.OrderFactory;
import com.kamillo.task.scheduler.domain.seats.SeatFacade;
import com.kamillo.task.scheduler.infrastructure.api.BlockSeatParams;
import com.kamillo.task.scheduler.infrastructure.saga.MyStateMachineFactory;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static com.kamillo.task.scheduler.domain.saga.SagaSeatEvents.BLOCK_EVENT;
import static com.kamillo.task.scheduler.domain.saga.SagaSeatEvents.PAYMENT_DONE_EVENT;
import static com.kamillo.task.scheduler.domain.saga.SagaSeatEvents.PAYMENT_REJECTED_EVENT;
import static com.kamillo.task.scheduler.domain.saga.SagaSeatEvents.START_PAYMENT_EVENT;

@Component
public class SeatSagaFacade {

    private final SeatFacade seatFacade;
    private final OrderFactory orderFactory;
    private final MessagesSender<SagaSeatEvents> messagesSender;
    private final MyStateMachineFactory myStateMachineFactory;

    public SeatSagaFacade(SeatFacade seatFacade, OrderFactory orderFactory, MessagesSender<SagaSeatEvents> messagesSender, MyStateMachineFactory myStateMachineFactory) {
        this.seatFacade = seatFacade;
        this.orderFactory = orderFactory;
        this.messagesSender = messagesSender;
        this.myStateMachineFactory = myStateMachineFactory;
    }

    public UUID blockSeat(BlockSeatParams params) {
        if (!seatFacade.checkIfSeatIsFree(params))
            throw new SeatIsNotFreeException();
        OrderDomain order = new OrderDomain(UUID.randomUUID());
        StateMachine<SagaSeatEnum, SagaSeatEvents> stateMachine = myStateMachineFactory.build(order);
        stateMachine.sendEvent(messagesSender.create(BLOCK_EVENT, order, params));
        return order.getOrderId();
    }

    public void startPayment(UUID orderId) {
        OrderDomain order = orderFactory.getOrder(orderId);
        StateMachine<SagaSeatEnum, SagaSeatEvents> stateMachine = myStateMachineFactory.build(order);

        stateMachine.sendEvent(messagesSender.create(START_PAYMENT_EVENT, order));
    }

    public void acceptedPayment(UUID orderId) {
        OrderDomain order = orderFactory.getOrder(orderId);
        StateMachine<SagaSeatEnum, SagaSeatEvents> stateMachine = myStateMachineFactory.build(order);

        stateMachine.sendEvent(messagesSender.create(PAYMENT_DONE_EVENT, order));
    }

    public void rejectedPayment(UUID orderId) {
        OrderDomain order = orderFactory.getOrder(orderId);
        StateMachine<SagaSeatEnum, SagaSeatEvents> stateMachine = myStateMachineFactory.build(order);

        stateMachine.sendEvent(messagesSender.create(PAYMENT_REJECTED_EVENT, order));
    }

}
