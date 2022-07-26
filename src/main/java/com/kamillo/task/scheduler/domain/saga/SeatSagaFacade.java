package com.kamillo.task.scheduler.domain.saga;

import com.kamillo.task.scheduler.domain.OrderDomain;
import com.kamillo.task.scheduler.infrastructure.api.BlockSeatParams;
import com.kamillo.task.scheduler.infrastructure.order.NoSuchOrderException;
import com.kamillo.task.scheduler.infrastructure.order.PostgresOrderRepository;
import com.kamillo.task.scheduler.infrastructure.saga.StateMachineFactory;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static com.kamillo.task.scheduler.domain.saga.SagaSeatEvents.*;

@Component
public class SeatSagaFacade {

    private final SeatFacade seatFacade;
    private final PostgresOrderRepository postgresOrderRepository;
    private final MessagesSender<SagaSeatEvents> messagesSender;
    private final StateMachineFactory stateMachineFactory;

    public SeatSagaFacade(SeatFacade seatFacade, PostgresOrderRepository postgresOrderRepository, MessagesSender<SagaSeatEvents> messagesSender, StateMachineFactory stateMachineFactory) {
        this.seatFacade = seatFacade;
        this.postgresOrderRepository = postgresOrderRepository;
        this.messagesSender = messagesSender;
        this.stateMachineFactory = stateMachineFactory;
    }

    public UUID blockSeat(BlockSeatParams params) {
        if (!seatFacade.checkIfSeatIsFree(params.getRowId(), params.getNumberId()))
            throw new SeatIsNotFreeException();
        OrderDomain order = new OrderDomain(UUID.randomUUID());
        StateMachine<SagaSeatEnum, SagaSeatEvents> stateMachine = stateMachineFactory.build(order);
        stateMachine.sendEvent(messagesSender.create(BLOCK_EVENT, order));
        return order.getOrderId();
    }

    public void startPayment(UUID orderId) {
        OrderDomain order = postgresOrderRepository.getOrder(orderId)
                .orElseThrow(NoSuchOrderException::new);
        StateMachine<SagaSeatEnum, SagaSeatEvents> stateMachine = stateMachineFactory.build(order);

        stateMachine.sendEvent(messagesSender.create(START_PAYMENT_EVENT, order));
    }

    public void acceptedPayment(UUID orderId) {
        OrderDomain order = postgresOrderRepository.getOrder(orderId)
                .orElseThrow(NoSuchOrderException::new);
        StateMachine<SagaSeatEnum, SagaSeatEvents> stateMachine = stateMachineFactory.build(order);

        stateMachine.sendEvent(messagesSender.create(PAYMENT_DONE_EVENT, order));
    }

    public void rejectedPayment(UUID orderId) {
        OrderDomain order = postgresOrderRepository.getOrder(orderId)
                .orElseThrow(NoSuchOrderException::new);
        StateMachine<SagaSeatEnum, SagaSeatEvents> stateMachine = stateMachineFactory.build(order);

        stateMachine.sendEvent(messagesSender.create(PAYMENT_REJECTED_EVENT, order));
    }

}
