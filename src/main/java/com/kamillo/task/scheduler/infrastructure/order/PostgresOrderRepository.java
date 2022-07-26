package com.kamillo.task.scheduler.infrastructure.order;

import com.kamillo.task.scheduler.domain.OrderDomain;
import com.kamillo.task.scheduler.domain.order.OrderRepository;
import com.kamillo.task.scheduler.domain.saga.SagaSeatEnum;
import org.springframework.stereotype.Service;

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
    public OrderDomain updateOrderState(UUID orderId, SagaSeatEnum state) {
        PostgresOrder postgresOrder = orderRepo.getByOrderID(orderId).orElseThrow(NoSuchOrderException::new);
        postgresOrder.setOrderState(state);
        return orderMapper.toOrderDomain(orderRepo.save(postgresOrder));
    }

    @Override
    public void saveOrUpdateOrderState(UUID orderId, SagaSeatEnum state) {
        Optional<PostgresOrder> optionalOrder = orderRepo.getByOrderID(orderId);
        optionalOrder.ifPresentOrElse(postgresOrder -> {
                    postgresOrder.setOrderState(state);
                    orderMapper.toOrderDomain(orderRepo.save(postgresOrder));
                },
                () -> saveOrder(new OrderDomain(orderId, state))
        );
    }

    private OrderDomain saveOrder(OrderDomain domain) {
        PostgresOrder saved = orderRepo.save(orderMapper.toOrder(domain));
        return orderMapper.toOrderDomain(saved);
    }

}
