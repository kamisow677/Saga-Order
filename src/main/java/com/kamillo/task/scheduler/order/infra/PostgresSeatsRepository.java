package com.kamillo.task.scheduler.infrastructure.seats;

import com.kamillo.task.scheduler.domain.SeatsDomain;
import com.kamillo.task.scheduler.domain.seats.SeatsRepository;
import com.kamillo.task.scheduler.infrastructure.api.BlockSeatParams;
import com.kamillo.task.scheduler.order.infra.GeneratedOrderRepo;
import com.kamillo.task.scheduler.order.infra.PostgresOrder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertTrue;

@Service
public class PostgresSeatsRepository implements SeatsRepository {

    private final GeneratedSeatsRepo seatsRepo;
    private final GeneratedOrderRepo orderRepository;
    private final PostgresSeatsMapper mapper;

    public PostgresSeatsRepository(GeneratedSeatsRepo seatsRepo, GeneratedOrderRepo orderRepository, PostgresSeatsMapper mapper) {
        this.seatsRepo = seatsRepo;
        this.orderRepository = orderRepository;
        this.mapper = mapper;
    }

    @Override
    public boolean areFree(BlockSeatParams blockSeatParams) {
        return seatsRepo.findByNumberIdAndRow(blockSeatParams.numberId(), blockSeatParams.rowId())
                .orElseThrow(NoSuchElementException::new)
                .isFree();
    }

    @Override
    @Transactional
    public boolean freeSeats(UUID orderId) {
        assertTrue(TransactionSynchronizationManager.isActualTransactionActive());
        seatsRepo.freeSeat(orderId);
        PostgresSeats byOrderOrderId = seatsRepo.findByOrderOrderId(orderId).orElseThrow(NoSuchElementException::new);
        byOrderOrderId.setFree(true);
        return true;
    }

}
