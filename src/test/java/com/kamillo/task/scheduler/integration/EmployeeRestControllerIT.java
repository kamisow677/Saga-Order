package com.kamillo.task.scheduler.integration;

import com.kamillo.task.scheduler.domain.saga.SagaSeatEnum;
import com.kamillo.task.scheduler.infrastructure.api.BlockSeatParams;
import com.kamillo.task.scheduler.infrastructure.order.GeneratedOrderRepo;
import com.kamillo.task.scheduler.infrastructure.order.PostgresOrder;
import com.kamillo.task.scheduler.infrastructure.task.GeneratedTaskRepo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;


public class EmployeeRestControllerIT extends BaseIT {

    @Autowired
    private GeneratedOrderRepo orderRepo;

    @Autowired
    private GeneratedTaskRepo taskRepo;

    @AfterEach
    public void clean() {
        orderRepo.deleteAll();
        taskRepo.deleteAll();
    }

    @Test
    public void shouldBlockSeatAndStartPayment() {
        // given
            BlockSeatParams blockSeatParams = new BlockSeatParams(1, 1);

        // when
            ResponseEntity<String> blockResponse = post("/public/block",  String.class, Map.of(), blockSeatParams);
            Optional<PostgresOrder> byOrderID = orderRepo.getByOrderID(UUID.fromString(Objects.requireNonNull(blockResponse.getBody())));
            UUID orderId = byOrderID.orElseThrow().getOrderId();

        // then
            assertEquals(SagaSeatEnum.SEAT_BLOCKED, orderRepo.getByOrderID(orderId).orElseThrow().getOrderState());
            assertEquals(blockResponse.getStatusCode(), HttpStatus.OK);

        // and when
            ResponseEntity<String> startResponse = startPaymentRequest(orderId);

        // them
            PostgresOrder order = orderRepo.getByOrderID(orderId).orElseThrow();
            assertEquals(SagaSeatEnum.SEAT_PAYMENT_PENDING, order.getOrderState());
            assertEquals(startResponse.getStatusCode(), HttpStatus.OK);
            assertEquals(1, order.getSeats().getNumber());
            assertEquals(1, order.getSeats().getRow());
            assertFalse(order.getSeats().isFree());
    }

    @Test
    public void shouldThrowExceptionWhenNoSuchOrder() {
        // can not start payment on nonexisting order
        // when
            UUID nonExistingOrderId = UUID.randomUUID();
            ResponseEntity<String> startResponse = startPaymentRequest(nonExistingOrderId);
            assertEquals(startResponse.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void shouldNotStartPaymentIfSeatWasNotBlocked() {
        // given
            UUID orderId = UUID.randomUUID();
            //seat is free now one blocked it
            orderRepo.save(PostgresOrder.builder().orderId(orderId).orderState(SagaSeatEnum.SEAT_FREE).build());

        // when
        // trying to start payment on unblocked seat
            ResponseEntity<String> response = startPaymentRequest(orderId);

        //then
        assertEquals(
            SagaSeatEnum.SEAT_FREE,
            orderRepo.getByOrderID(orderId)
                .orElseThrow(() -> new AssertionError("Test is corrupted"))
                .getOrderState()
        );
        assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    private ResponseEntity<String> startPaymentRequest(UUID orderId) {
        return post("/public/start_payment",  String.class, Map.of(ORDER_ID_PARAM , orderId.toString()), null);
    }


}