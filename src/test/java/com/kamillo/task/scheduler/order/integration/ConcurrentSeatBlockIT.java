package com.kamillo.task.scheduler.order.integration;

import com.kamillo.task.scheduler.domain.SeatsDomain;
import com.kamillo.task.scheduler.domain.saga.SagaSeatEnum;
import com.kamillo.task.scheduler.infrastructure.api.BlockSeatParams;
import com.kamillo.task.scheduler.order.domain.OrderDomain;
import com.kamillo.task.scheduler.order.domain.OrderFacade;
import com.kamillo.task.scheduler.order.domain.OrderRepository;
import com.kamillo.task.scheduler.order.infra.GeneratedOrderRepo;
import com.kamillo.task.scheduler.order.infra.PostgresOrderMapper;
import com.kamillo.task.scheduler.order.infra.PostgresSeats;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class ConcurrentSeatBlockIT extends BaseIT {

    @Autowired
    OrderFacade orderFacade;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    GeneratedOrderRepo generatedOrderRepo;


    @Test
    public void twoUsersBlockSameSeatTransaction() {
        //given
        UUID uuid1 = UUID.randomUUID();
        UUID uuid2 = UUID.randomUUID();
        BlockSeatParams blockSeatParams1 = new BlockSeatParams(1, 1);
        BlockSeatParams blockSeatParams2 = new BlockSeatParams(1, 1);
        CompletableFuture<OrderDomain> future1
                = CompletableFuture.supplyAsync(() -> orderFacade.issueNewOrderWithTransaction(new OrderDomain(uuid1), blockSeatParams1));
        CompletableFuture<OrderDomain> future2
                = CompletableFuture.supplyAsync(() -> orderFacade.issueNewOrderWithTransaction(new OrderDomain(uuid2), blockSeatParams2));

        // when
        CompletableFuture.allOf(future1, future2).handle((result, ex) -> {
            Assertions.assertSame(ex.getCause().getClass(), CannotAcquireLockException.class);
            System.out.println("Asserted twoUsersBlockSameSeat");
            return ex;
        }).join();

        //then
        List<OrderDomain> all = generatedOrderRepo.findAll().stream().map(new PostgresOrderMapper()::apply).collect(Collectors.toList());
        System.out.println(all);
    }

    @Test
    public void twoUsersChangeOrderState() {
        //given
        UUID uuid1 = UUID.randomUUID();
        orderRepository.saveOrUpdateOrderState(uuid1, SagaSeatEnum.SEAT_FREE);
        List<OrderDomain> all = generatedOrderRepo.findAll().stream().map(new PostgresOrderMapper()::apply).collect(Collectors.toList());
        System.out.println(all);


        CompletableFuture<Void> future1
                = CompletableFuture.runAsync(() -> orderRepository.saveOrUpdateOrderState(uuid1, SagaSeatEnum.SEAT_ALLOCATED));
        CompletableFuture<Void> future2
                = CompletableFuture.runAsync(() -> orderRepository.saveOrUpdateOrderState(uuid1, SagaSeatEnum.SEAT_PAYMENT_REJECTED));

        // when
        CompletableFuture.allOf(future1, future2).handle((result, ex) -> {
            Assertions.assertSame(ex.getCause().getClass(), ObjectOptimisticLockingFailureException.class);
            System.out.println("Asserted twoUsersChangeOrderState");
            return ex;
        }).join();

        //then
        all = generatedOrderRepo.findAll().stream().map(new PostgresOrderMapper()::apply).collect(Collectors.toList());
        System.out.println(all);
    }


    @Test
    public void shouldNotAllowBlockBlockedSeat() {
        //given
        UUID uuid1 = UUID.randomUUID();
        UUID uuid2 = UUID.randomUUID();
        BlockSeatParams blockSeatParams1 = new BlockSeatParams(1, 1);
        BlockSeatParams blockSeatParams2 = new BlockSeatParams(1, 1);
        orderFacade.issueNewOrderWithTransaction(new OrderDomain(uuid1), blockSeatParams1);

        // when
        Assertions.assertThrows(com.kamillo.task.scheduler.order.infra.SeatIsNotFreeException.class, () -> {
            orderFacade.issueNewOrderWithTransaction(new OrderDomain(uuid2), blockSeatParams2);
        });
    }
}
