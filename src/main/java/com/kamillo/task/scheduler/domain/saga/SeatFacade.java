package com.kamillo.task.scheduler.domain.saga;

import org.springframework.stereotype.Component;

@Component
public class SeatFacade {

    public boolean checkIfSeatIsFree(String seatRow, String seatNumber) {
        return true;
    }

}
