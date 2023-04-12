package com.kamillo.task.scheduler.order.infra;

import com.kamillo.task.scheduler.order.domain.SeatsRepository;
import com.kamillo.task.scheduler.infrastructure.api.BlockSeatParams;
import jakarta.persistence.LockModeType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.NoSuchElementException;
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
        return seatsRepo.findByNumberIdAndRow(blockSeatParams.numberId(), blockSeatParams.rowId(), LockModeType.PESSIMISTIC_READ)
                .orElseThrow(NoSuchElementException::new)
                .isFree();
    }

    @Override
    @Transactional
    public boolean setFree(UUID orderId) {
        assertTrue(TransactionSynchronizationManager.isActualTransactionActive());
        seatsRepo.freeSeat(orderId);
        PostgresSeats byOrderOrderId = seatsRepo.findByOrderOrderId(orderId).orElseThrow(NoSuchElementException::new);
        byOrderOrderId.setFree(true);
        return true;
    }

    @Override
    public boolean exists(BlockSeatParams blockSeatParams) {
        return seatsRepo.findByNumberIdAndRow(blockSeatParams.numberId(), blockSeatParams.rowId())
                .isPresent();
    }

}
