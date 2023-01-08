package com.kamillo.task.scheduler.infrastructure.seats;

import com.kamillo.task.scheduler.domain.OrderDomain;
import com.kamillo.task.scheduler.domain.SeatsDomain;
import com.kamillo.task.scheduler.domain.seats.SeatsRepository;
import com.kamillo.task.scheduler.infrastructure.api.BlockSeatParams;
import com.kamillo.task.scheduler.infrastructure.order.GeneratedOrderRepo;
import com.kamillo.task.scheduler.infrastructure.order.OrderMapper;
import com.kamillo.task.scheduler.infrastructure.order.PostgresOrder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class PostgresSeatsRepository implements SeatsRepository {

    private final GeneratedSeatsRepo seatsRepo;
    private final GeneratedOrderRepo orderRepository;
    private final SeatsMapper mapper;
    private final OrderMapper orderMapper;

    public PostgresSeatsRepository(GeneratedSeatsRepo seatsRepo, GeneratedOrderRepo orderRepository, SeatsMapper mapper, OrderMapper orderMapper) {
        this.seatsRepo = seatsRepo;
        this.orderRepository = orderRepository;
        this.mapper = mapper;
        this.orderMapper = orderMapper;
    }

    @Transactional
    public SeatsDomain saveSeats(SeatsDomain domain, UUID orderId) {
        Seats seats = mapper.toSeats(domain);
        PostgresOrder postgresOrder = orderRepository.getByOrderID(orderId).orElseThrow(NoSuchElementException::new);
        seats.setOrder(postgresOrder);
        Seats save = seatsRepo.save(seats);
        return mapper.toSeatsDomain(save);
    }

    @Override
    public boolean areFree(BlockSeatParams blockSeatParams) {
        return seatsRepo.findByNumberIdAndRow(blockSeatParams.numberId(), blockSeatParams.rowId())
                .orElseThrow(NoSuchElementException::new)
                .isFree();
    }

    @Override
    public void freeSeats(UUID orderId) {
        seatsRepo.freeSeat(orderId);
    }

    @Override
    @Transactional
    public OrderDomain blockSeats(UUID orderId, BlockSeatParams blockSeatParams) {
        Seats seats = seatsRepo.findByNumberIdAndRow(blockSeatParams.numberId(), blockSeatParams.rowId()).orElseThrow(NoSuchElementException::new);
        seats.setFree(false);
        PostgresOrder postgresOrder = orderRepository.getByOrderID(orderId).orElseThrow(NoSuchElementException::new);
        postgresOrder.setSeats(seats);
        PostgresOrder updated = orderRepository.save(postgresOrder);
        return orderMapper.toOrderDomain(updated);
    }
}
