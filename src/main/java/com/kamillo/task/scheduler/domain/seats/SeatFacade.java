package com.kamillo.task.scheduler.domain.seats;

import com.kamillo.task.scheduler.domain.SeatsDomain;
import com.kamillo.task.scheduler.domain.order.OrderRepository;
import com.kamillo.task.scheduler.infrastructure.api.BlockSeatParams;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class SeatFacade {

    private final SeatsRepository seatsRepository;
    private final OrderRepository orderRepository;

    public SeatFacade(SeatsRepository seatsRepository, OrderRepository orderRepository) {
        this.seatsRepository = seatsRepository;
        this.orderRepository = orderRepository;
    }


    public boolean checkIfSeatIsFree(BlockSeatParams blockSeatParams) {
        return seatsRepository.areFree(blockSeatParams);
    }

    public void saveSeats(BlockSeatParams blockSeatParams, UUID orderId) {
        SeatsDomain seatsDomain = SeatsDomain.builder()
                .row(blockSeatParams.rowId())
                .number(blockSeatParams.numberId())
                .free(false)
                .build();
        seatsRepository.saveSeats(seatsDomain, orderId);
    }

    public void blockSeats(BlockSeatParams blockSeatParams, UUID orderId) {
        seatsRepository.blockSeats(orderId, blockSeatParams);
    }

    public void freeSeats(UUID orderId) {
        seatsRepository.freeSeats(orderId);
    }
}
