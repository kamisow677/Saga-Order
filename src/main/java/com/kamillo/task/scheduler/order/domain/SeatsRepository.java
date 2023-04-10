package com.kamillo.task.scheduler.domain.seats;

import com.kamillo.task.scheduler.infrastructure.api.BlockSeatParams;

import java.util.UUID;

public interface SeatsRepository {

    boolean areFree(BlockSeatParams blockSeatParams);

    boolean freeSeats(UUID orderId);
}
