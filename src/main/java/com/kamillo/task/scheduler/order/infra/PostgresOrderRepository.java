package com.kamillo.task.scheduler.order.infra;

import com.kamillo.task.scheduler.domain.saga.SagaSeatEnum;
import com.kamillo.task.scheduler.infrastructure.api.BlockSeatParams;
import com.kamillo.task.scheduler.order.domain.OrderDomain;
import com.kamillo.task.scheduler.order.domain.OrderRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;


@Component
class PostgresOrderRepository implements OrderRepository {

    private final PostgresOrderMapper postgresOrderMapper;
    private final GeneratedOrderRepo orderRepo;
    private final GeneratedSeatsRepo seatsRepo;

    public PostgresOrderRepository(PostgresOrderMapper postgresOrderMapper, GeneratedOrderRepo orderRepo, GeneratedSeatsRepo seatsRepo) {
        this.postgresOrderMapper = postgresOrderMapper;
        this.orderRepo = orderRepo;
        this.seatsRepo = seatsRepo;
    }

    @Override
    public Optional<OrderDomain> getOrder(UUID orderId) {
        return orderRepo.getByOrderID(orderId).map(postgresOrderMapper::toDomain);
    }

    @Override
    public OrderDomain updateOrderState(UUID orderId, SagaSeatEnum state) {
        PostgresOrder postgresOrder = orderRepo.getByOrderID(orderId).orElseThrow(() -> new NoSuchOrderException(orderId));
        postgresOrder.setOrderState(state);
        return postgresOrderMapper.toDomain(orderRepo.save(postgresOrder));
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void saveOrUpdateOrderState(UUID orderId, SagaSeatEnum state) {
        Optional<PostgresOrder> optionalOrder = orderRepo.getByOrderID(orderId);
        optionalOrder.ifPresentOrElse(
                postgresOrder -> postgresOrder.setOrderState(state),
                () -> orderRepo.save(PostgresOrder.builder().orderId(orderId).orderState(state).build())
        );
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public OrderDomain createOrderWithBlockedSeats(OrderDomain domain, BlockSeatParams blockSeatParams) {
        PostgresSeats byNumberIdAndRow = seatsRepo.findByNumberIdAndRow(blockSeatParams.numberId(), blockSeatParams.rowId())
                .orElseThrow(NoSuchElementException::new);
        if (!byNumberIdAndRow.isFree()) {
            throw new SeatIsNotFreeException();
        }
        byNumberIdAndRow.setFree(false);
        PostgresOrder postgresOrder = postgresOrderMapper.toEntity(domain);
        postgresOrder.setSeats(byNumberIdAndRow);
        orderRepo.save(postgresOrder);
        return postgresOrderMapper.toDomain(postgresOrder);
    }

    public OrderDomain saveOrder(OrderDomain domain) {
        PostgresOrder saved = orderRepo.save(postgresOrderMapper.toEntity(domain));
        return postgresOrderMapper.toDomain(saved);
    }

    @Override
    @Transactional
    public boolean decoupleSeats(UUID orderId) {
        PostgresOrder order = orderRepo.findByOrderId(orderId).orElseThrow(NoSuchElementException::new);
        order.deleteSeats();
        return true;
    }

}
