package com.kamillo.task.scheduler.infrastructure.order;

import com.kamillo.task.scheduler.domain.OrderDomain;
import com.kamillo.task.scheduler.domain.SeatsDomain;
import com.kamillo.task.scheduler.domain.order.OrderRepository;
import com.kamillo.task.scheduler.domain.saga.SagaSeatEnum;
import com.kamillo.task.scheduler.infrastructure.api.BlockSeatParams;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

@Service
public class PostgresOrderRepository implements OrderRepository {

    private final OrderMapper orderMapper;
    private final GeneratedOrderRepo orderRepo;

    public PostgresOrderRepository(OrderMapper orderMapper, GeneratedOrderRepo orderRepo) {
        this.orderMapper = orderMapper;
        this.orderRepo = orderRepo;
    }

    @Override
    public Optional<OrderDomain> getOrder(UUID orderId) {
        return orderRepo.getByOrderID(orderId).map(orderMapper::toOrderDomain);
    }

    @Override
    @Transactional
    public OrderDomain updateOrderState(UUID orderId, SagaSeatEnum state) {
        PostgresOrder postgresOrder = orderRepo.getByOrderID(orderId).orElseThrow(() -> new NoSuchOrderException(orderId));
        postgresOrder.setOrderState(state);
        return orderMapper.toOrderDomain(orderRepo.save(postgresOrder));
    }

    @Override
    @Transactional
    public void saveOrUpdateOrderState(UUID orderId, SagaSeatEnum state) {
        Optional<PostgresOrder> optionalOrder = orderRepo.getByOrderID(orderId);
        optionalOrder.ifPresentOrElse(postgresOrder -> {
                    postgresOrder.setOrderState(state);
                    orderMapper.toOrderDomain(orderRepo.save(postgresOrder));
                },
                () -> saveOrder(new OrderDomain(orderId, state))
        );
    }

    public void saveOrder(OrderDomain domain) {
        PostgresOrder saved = orderRepo.save(orderMapper.toOrder(domain));
        orderMapper.toOrderDomain(saved);
    }

}
