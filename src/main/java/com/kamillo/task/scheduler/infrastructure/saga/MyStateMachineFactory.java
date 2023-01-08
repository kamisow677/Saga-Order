package com.kamillo.task.scheduler.infrastructure.saga;

import com.kamillo.task.scheduler.domain.OrderDomain;
import com.kamillo.task.scheduler.domain.saga.SagaSeatEnum;
import com.kamillo.task.scheduler.domain.saga.SagaSeatEvents;
import com.kamillo.task.scheduler.infrastructure.order.PostgresOrderRepository;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.statemachine.support.StateMachineInterceptorAdapter;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

import static com.kamillo.task.scheduler.infrastructure.saga.MessagesFactory.ORDER_ID_HEADER;

@Component
public class MyStateMachineFactory {

    private final org.springframework.statemachine.config.StateMachineFactory<SagaSeatEnum, SagaSeatEvents> stateMachineFactory;
    private final PostgresOrderRepository postgresOrderRepository;

    public MyStateMachineFactory(org.springframework.statemachine.config.StateMachineFactory<SagaSeatEnum, SagaSeatEvents> stateMachineFactory, PostgresOrderRepository postgresOrderRepository) {
        this.stateMachineFactory = stateMachineFactory;
        this.postgresOrderRepository = postgresOrderRepository;
    }

    public StateMachine<SagaSeatEnum, SagaSeatEvents> build(OrderDomain order) {

        StateMachine<SagaSeatEnum, SagaSeatEvents> sm = this.stateMachineFactory.getStateMachine(order.getOrderId());
        sm.stopReactively();
        sm.getStateMachineAccessor()
                .doWithAllRegions(sma -> {
                    sma.addStateMachineInterceptor(new SeatChangeListener());
                    sma.resetStateMachine(new DefaultStateMachineContext<>(order.getOrderState(), null, null, sm.getExtendedState()));
                });
        sm.start();
        sm.getState();
        return sm;
    }

    private class SeatChangeListener extends StateMachineInterceptorAdapter<SagaSeatEnum, SagaSeatEvents> {

        @Override
        public void postStateChange(State<SagaSeatEnum, SagaSeatEvents> state, Message<SagaSeatEvents> message, Transition<SagaSeatEnum, SagaSeatEvents> transition, StateMachine<SagaSeatEnum, SagaSeatEvents> stateMachine, StateMachine<SagaSeatEnum, SagaSeatEvents> rootStateMachine) {
            Optional.ofNullable(message)
                    .flatMap(msg -> Optional.ofNullable(msg.getHeaders().getOrDefault(ORDER_ID_HEADER, -1L)))
                    .ifPresent(
                            orderId -> postgresOrderRepository.saveOrUpdateOrderState((UUID) orderId, state.getId())
                    );
        }

    }

}
