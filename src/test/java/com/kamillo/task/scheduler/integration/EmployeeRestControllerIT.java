package com.kamillo.task.scheduler.integration;

import com.kamillo.task.scheduler.domain.saga.SagaSeatEnum;
import com.kamillo.task.scheduler.infrastructure.api.BlockSeatParams;
import com.kamillo.task.scheduler.infrastructure.order.NoSuchOrderException;
import com.kamillo.task.scheduler.infrastructure.order.PostgresOrder;
import com.kamillo.task.scheduler.infrastructure.order.GeneratedOrderRepo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class EmployeeRestControllerIT extends BaseIT {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private GeneratedOrderRepo orderRepo;

    @AfterEach
    public void clean() {
        orderRepo.deleteAll();
    }

    @Test
    public void shouldBlockSeat() {
        // given
            BlockSeatParams blockSeatParams = new BlockSeatParams("1", "1");
        // when
            ResponseEntity<String> blockResponse = post("/public/block",  String.class, Map.of(), blockSeatParams);
        // then
            Optional<PostgresOrder> byOrderID = orderRepo.getByOrderID(UUID.fromString(Objects.requireNonNull(blockResponse.getBody())));
            assertEquals(blockResponse.getStatusCode(), HttpStatus.OK);
        // and
            UUID orderId = byOrderID.orElseThrow().getOrderId();
            ResponseEntity<String> startResponse = post("/public/start_payment",  String.class, Map.of("orderId" , orderId.toString()), null);

            assertEquals(SagaSeatEnum.SEAT_PAYMENT_PENDING, orderRepo.getByOrderID(orderId).get().getOrderState());
            assertEquals(startResponse.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void shouldThrowExceptionWhenNoSuchOrder() {
        // when
        String orderId = "NonExistingOrderId";
        assertThrows(NoSuchOrderException.class, () -> {
            ResponseEntity<String> startResponse = post("/public/start_payment",  String.class, Map.of("orderId" , orderId), null);
        });
    }

}