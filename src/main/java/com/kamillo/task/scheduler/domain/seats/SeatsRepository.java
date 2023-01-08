package com.kamillo.task.scheduler.domain.seats;

import com.kamillo.task.scheduler.domain.OrderDomain;
import com.kamillo.task.scheduler.domain.SeatsDomain;
import com.kamillo.task.scheduler.infrastructure.api.BlockSeatParams;

import java.util.UUID;

public interface SeatsRepository {

    SeatsDomain saveSeats(SeatsDomain domain, UUID orderId);

    boolean areFree(BlockSeatParams blockSeatParams);

    void freeSeats(UUID orderId);

    OrderDomain blockSeats(UUID orderId, BlockSeatParams blockSeatParams);
}
