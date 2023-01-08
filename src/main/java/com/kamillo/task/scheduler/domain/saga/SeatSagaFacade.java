package com.kamillo.task.scheduler.domain.saga;

import com.kamillo.task.scheduler.domain.OrderDomain;
import com.kamillo.task.scheduler.domain.seats.SeatFacade;
import com.kamillo.task.scheduler.infrastructure.api.BlockSeatParams;
import com.kamillo.task.scheduler.infrastructure.order.NoSuchOrderException;
import com.kamillo.task.scheduler.infrastructure.order.PostgresOrderRepository;
import com.kamillo.task.scheduler.infrastructure.saga.MyStateMachineFactory;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static com.kamillo.task.scheduler.domain.saga.SagaSeatEvents.*;

@Component
public class SeatSagaFacade {

    private final SeatFacade seatFacade;
    private final PostgresOrderRepository postgresOrderRepository;
    private final MessagesSender<SagaSeatEvents> messagesSender;
    private final MyStateMachineFactory myStateMachineFactory;

    public SeatSagaFacade(SeatFacade seatFacade, PostgresOrderRepository postgresOrderRepository, MessagesSender<SagaSeatEvents> messagesSender, MyStateMachineFactory myStateMachineFactory) {
        this.seatFacade = seatFacade;
        this.postgresOrderRepository = postgresOrderRepository;
        this.messagesSender = messagesSender;
        this.myStateMachineFactory = myStateMachineFactory;
    }

    public UUID blockSeat(BlockSeatParams params) {
        if (!seatFacade.checkIfSeatIsFree(params))
            throw new SeatIsNotFreeException();
        OrderDomain order = new OrderDomain(UUID.randomUUID());
        postgresOrderRepository.saveOrder(order);
        StateMachine<SagaSeatEnum, SagaSeatEvents> stateMachine = myStateMachineFactory.build(order);

        stateMachine.sendEvent(messagesSender.create(BLOCK_EVENT, order, params));
        return order.getOrderId();
    }

    public void startPayment(UUID orderId) {
        OrderDomain order = postgresOrderRepository.getOrder(orderId)
                .orElseThrow(() -> new NoSuchOrderException(orderId));
        StateMachine<SagaSeatEnum, SagaSeatEvents> stateMachine = myStateMachineFactory.build(order);

        stateMachine.sendEvent(messagesSender.create(START_PAYMENT_EVENT, order));
    }

    public void acceptedPayment(UUID orderId) {
        OrderDomain order = postgresOrderRepository.getOrder(orderId)
                .orElseThrow(() -> new NoSuchOrderException(orderId));
        StateMachine<SagaSeatEnum, SagaSeatEvents> stateMachine = myStateMachineFactory.build(order);

        stateMachine.sendEvent(messagesSender.create(PAYMENT_DONE_EVENT, order));
    }

    public void rejectedPayment(UUID orderId) {
        OrderDomain order = postgresOrderRepository.getOrder(orderId)
                .orElseThrow(() -> new NoSuchOrderException(orderId));
        StateMachine<SagaSeatEnum, SagaSeatEvents> stateMachine = myStateMachineFactory.build(order);

        stateMachine.sendEvent(messagesSender.create(PAYMENT_REJECTED_EVENT, order));
    }

}
