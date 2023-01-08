package com.kamillo.task.scheduler.infrastructure.seats;

import com.kamillo.task.scheduler.domain.SeatsDomain;
import org.springframework.stereotype.Component;

@Component
public class SeatsMapper {

    public Seats toSeats(SeatsDomain domain) {
        return Seats.builder()
                .id(domain.getId())
                .row(domain.getRow())
                .number(domain.getRow())
                .free(domain.isFree())
                .build();
    }

    public SeatsDomain toSeatsDomain(Seats seats) {
        return SeatsDomain.builder()
                .id(seats.getId())
                .row(seats.getRow())
                .number(seats.getRow())
                .free(seats.isFree())
                .build();
    }

}
