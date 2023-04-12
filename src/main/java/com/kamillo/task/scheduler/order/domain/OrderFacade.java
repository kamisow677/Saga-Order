package com.kamillo.task.scheduler.order.domain;

import com.kamillo.task.scheduler.domain.saga.SeatIsDoesNotExistsException;
import com.kamillo.task.scheduler.infrastructure.api.BlockSeatParams;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

@Service
public class OrderFacade {

    Logger logger = LoggerFactory.getLogger(OrderFacade.class);


    private final OrderRepository orderRepository;
    private final SeatsRepository seatsRepository;

    public OrderFacade(
            OrderRepository orderRepository,
            SeatsRepository seatsRepository
    ) {
        this.orderRepository = orderRepository;
        this.seatsRepository = seatsRepository;
    }

    public OrderDomain issueNewOrderWithTransaction(OrderDomain order, BlockSeatParams blockSeatParams) {
        if (!checkIfSeatExists(blockSeatParams))
            throw new SeatIsDoesNotExistsException();
        OrderDomain orderWithBlockedSeats = orderRepository.createOrderWithBlockedSeats(order, blockSeatParams);
        logger.info("New order was saved to database " + orderWithBlockedSeats);
        return  orderWithBlockedSeats;
    }

    public boolean rejectOrder(UUID orderId) {
        seatsRepository.setFree(orderId);
        return orderRepository.decoupleSeats(orderId);
    }

    private boolean checkIfSeatExists(BlockSeatParams blockSeatParams) {
        return seatsRepository.exists(blockSeatParams);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (OrderFacade) obj;
        return Objects.equals(this.orderRepository, that.orderRepository) &&
                Objects.equals(this.seatsRepository, that.seatsRepository);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderRepository, seatsRepository);
    }

    @Override
    public String toString() {
        return "OrderFacade[" +
                "orderRepository=" + orderRepository + ", " +
                "seatsRepository=" + seatsRepository + ']';
    }

}
