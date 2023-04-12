package com.kamillo.task.scheduler.order.infra;

import com.kamillo.task.scheduler.domain.SeatsDomain;
import com.kamillo.task.scheduler.order.domain.OrderDomain;
import org.springframework.cglib.core.internal.Function;
import org.springframework.stereotype.Component;

@Component
public class PostgresOrderMapper implements Function<PostgresOrder, OrderDomain> {

    PostgresOrder toEntity(OrderDomain domain) {
        return PostgresOrder.builder()
                .orderId(domain.getOrderId())
                .orderState(domain.getOrderState())
                .build();
    }

    OrderDomain toDomain(PostgresOrder postgresOrder) {
        return OrderDomain.builder()
                .orderId(postgresOrder.getOrderId())
                .orderState(postgresOrder.getOrderState())
                .seatsDomain(toDomain(postgresOrder.getSeats()))
                .version(postgresOrder.getVersion())
                .build();
    }

    SeatsDomain toDomain(PostgresSeats postgresSeats) {
        if (postgresSeats != null)
            return SeatsDomain.builder()
                    .id(postgresSeats.getId())
                    .number(postgresSeats.getNumber())
                    .row(postgresSeats.getRow())
                    .free(postgresSeats.isFree())
                    .build();
        else
            return null;
    }


    @Override
    public OrderDomain apply(PostgresOrder key) {
        return toDomain(key);
    }
}
