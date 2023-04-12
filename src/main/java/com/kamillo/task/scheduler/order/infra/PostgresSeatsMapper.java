package com.kamillo.task.scheduler.order.infra;

import com.kamillo.task.scheduler.domain.SeatsDomain;
import org.springframework.stereotype.Component;

@Component
public class PostgresSeatsMapper {

    public PostgresSeats toSeats(SeatsDomain domain) {
        return PostgresSeats.builder()
                .id(domain.getId())
                .row(domain.getRow())
                .number(domain.getRow())
                .free(domain.isFree())
                .build();
    }

    public SeatsDomain toSeatsDomain(PostgresSeats postgresSeats) {
        return SeatsDomain.builder()
                .id(postgresSeats.getId())
                .row(postgresSeats.getRow())
                .number(postgresSeats.getRow())
                .free(postgresSeats.isFree())
                .build();
    }

}
