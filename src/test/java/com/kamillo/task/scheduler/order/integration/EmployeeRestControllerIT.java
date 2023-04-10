package com.kamillo.task.scheduler.integration;

import com.kamillo.task.scheduler.domain.saga.SagaSeatEnum;
import com.kamillo.task.scheduler.infrastructure.api.BlockSeatParams;
import com.kamillo.task.scheduler.order.infra.GeneratedOrderRepo;
import com.kamillo.task.scheduler.order.infra.PostgresOrder;
import com.kamillo.task.scheduler.infrastructure.seats.GeneratedSeatsRepo;
import com.kamillo.task.scheduler.infrastructure.seats.PostgresSeats;
import com.kamillo.task.scheduler.infrastructure.task.GeneratedTaskRepo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Duration;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;


public class EmployeeRestControllerIT extends BaseIT {

    @Autowired
    private GeneratedOrderRepo orderRepo;

    @Autowired
    private GeneratedTaskRepo taskRepo;

    @Autowired
    private GeneratedSeatsRepo seatsRepo;

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
            assertEquals(1, order.getPostgresSeats().getNumber());
            assertEquals(1, order.getPostgresSeats().getRow());
            assertFalse(order.getPostgresSeats().isFree());
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

    @Test
    public void shouldRejectPaymentAndFreeSeet() {
        // given
        UUID orderId = UUID.randomUUID();
        //seat is free now one blocked it
        PostgresSeats postgresSeats = seatsRepo.findAll().get(0);
        postgresSeats.setFree(false);
        PostgresOrder order = PostgresOrder.builder().orderId(orderId).orderState(SagaSeatEnum.SEAT_PAYMENT_PENDING).build();
        order.setPostgresSeats(postgresSeats);
        orderRepo.save(order);

        // when
        // trying to start payment on unblocked seat
        ResponseEntity<String> response = rejectPayment(orderId);

        //then
        await()
            .atLeast(Duration.ZERO)
            .atMost(Duration.ofSeconds(20))
            .with()
            .pollInterval(Duration.ofSeconds(2))
            .untilAsserted(() -> {
                PostgresOrder orderChanged = orderRepo.findById(order.getId())
                        .orElseThrow(() -> new AssertionError("Test is corrupted"));
                PostgresSeats postgresSeatsChanged = seatsRepo.findById(postgresSeats.getId())
                        .orElseThrow(() -> new AssertionError("Test is corrupted"));
                assertEquals(SagaSeatEnum.SEAT_FREE, orderChanged.getOrderState());
                assertTrue(postgresSeatsChanged.isFree());
                assertEquals(response.getStatusCode(), HttpStatus.OK);
            });
    }

    private ResponseEntity<String> startPaymentRequest(UUID orderId) {
        return post("/public/start_payment",  String.class, Map.of(ORDER_ID_PARAM , orderId.toString()), null);
    }

    private ResponseEntity<String> rejectPayment(UUID orderId) {
        return post("/public/rejected_payment",  String.class, Map.of(ORDER_ID_PARAM , orderId.toString()), null);
    }

}