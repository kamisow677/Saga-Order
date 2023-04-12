package com.kamillo.task.scheduler.order.domain;

import com.kamillo.task.scheduler.infrastructure.api.BlockSeatParams;

import java.util.UUID;

public interface SeatsRepository {

    boolean areFree(BlockSeatParams blockSeatParams);

    boolean setFree(UUID orderId);

    boolean exists(BlockSeatParams blockSeatParams);
}
