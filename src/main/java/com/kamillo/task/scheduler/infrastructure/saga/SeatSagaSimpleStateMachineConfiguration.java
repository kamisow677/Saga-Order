package com.kamillo.task.scheduler.infrastructure.saga;

import com.kamillo.task.scheduler.domain.saga.SagaSeatEnum;
import com.kamillo.task.scheduler.domain.saga.SagaSeatEvents;
import com.kamillo.task.scheduler.infrastructure.order.PostgresOrderRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;

import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.kamillo.task.scheduler.domain.saga.SagaSeatEnum.*;
import static com.kamillo.task.scheduler.domain.saga.SagaSeatEvents.*;
import static com.kamillo.task.scheduler.domain.saga.SagaSeatEvents.PAYMENT_REJECTED_EVENT;
import static com.kamillo.task.scheduler.infrastructure.saga.MessagesFactory.ORDER_ID_HEADER;

@Configuration
@EnableStateMachineFactory
@Log4j2
public class SeatSagaSimpleStateMachineConfiguration
        extends StateMachineConfigurerAdapter<SagaSeatEnum, SagaSeatEvents> {

    @Override
    public void configure(StateMachineStateConfigurer<SagaSeatEnum, SagaSeatEvents> states)
            throws Exception {
        states
                .withStates()
                .initial(SEAT_FREE)
                .end(SEAT_ALLOCATED)
                .states(Arrays.stream(SagaSeatEnum.values()).collect(Collectors.toSet()));
    }

    @Override
    public void configure(StateMachineConfigurationConfigurer<SagaSeatEnum, SagaSeatEvents> config) throws Exception {
        StateMachineListenerAdapter<SagaSeatEnum, SagaSeatEvents> stateMachineListenerAdapter = new StateMachineListenerAdapter<>() {
            @Override
            public void stateChanged(State<SagaSeatEnum, SagaSeatEvents> from, State<SagaSeatEnum, SagaSeatEvents> to) {
                super.stateChanged(from, to);
                if (from != null)
                    log.info("State changed from: "+ from.getId() +" to: " + to.getId());
                else
                    log.info("State changed to: " + to.getId());
            }
        };
        config.withConfiguration()
                .autoStartup(false)
                .listener(stateMachineListenerAdapter);

    }

    @Override
    public void configure(
            StateMachineTransitionConfigurer<SagaSeatEnum, SagaSeatEvents> transitions)
            throws Exception {

        transitions
                .withExternal()
                    .source(SEAT_FREE).target(SEAT_BLOCKED)
                    .event(BLOCK_EVENT).action(
                        context -> {
                            UUID orderId = (UUID) context.getMessageHeaders().get(ORDER_ID_HEADER);
                            log.info("Order: " + orderId + " from state " + SEAT_FREE + " to " + SEAT_BLOCKED);
                        }
                    )
                .and().withExternal()
                    .source(SEAT_BLOCKED).target(SEAT_PAYMENT_PENDING)
                    .event(START_PAYMENT_EVENT).action(
                        context -> {
                            UUID orderId = (UUID) context.getMessageHeaders().get(ORDER_ID_HEADER);
                            log.info("Order: " + orderId + " from state " + SEAT_BLOCKED + " to " + SEAT_PAYMENT_PENDING);
                        }
                )
                .and().withExternal()
                    .source(SEAT_PAYMENT_PENDING).target(SEAT_ALLOCATED)
                    .event(PAYMENT_DONE_EVENT).action(
                        context -> {
                            UUID orderId = (UUID) context.getMessageHeaders().get(ORDER_ID_HEADER);
                            log.info("Order: " + orderId + " from state " + SEAT_PAYMENT_PENDING + " to " + SEAT_ALLOCATED);
                        }
                )
                .and().withExternal()
                    .source(SEAT_PAYMENT_PENDING).target(SEAT_FREE)
                    .event(PAYMENT_REJECTED_EVENT).action(
                        context -> {
                            UUID orderId = (UUID) context.getMessageHeaders().get(ORDER_ID_HEADER);
                            log.info("Order: " + orderId + " from state " + SEAT_PAYMENT_PENDING + " to " + SEAT_FREE);
                        }
                );
    }

}