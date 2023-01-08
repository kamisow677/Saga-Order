package com.kamillo.task.scheduler.infrastructure.api;

import com.kamillo.task.scheduler.domain.saga.SeatSagaFacade;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/public")
public class OrderSagaController {

    private final SeatSagaFacade seatSagaFacade;

    public OrderSagaController(SeatSagaFacade seatSagaFacade) {
        this.seatSagaFacade = seatSagaFacade;
    }

    @GetMapping("/test")
    public ResponseEntity runTestScheduler() {
        return  ResponseEntity.ok("Accessed test endpoint");
    }

    @PostMapping("/block")
    public ResponseEntity<String> blockSeat(@RequestBody BlockSeatParams params) {
        UUID orderId = seatSagaFacade.blockSeat(params);
        return  ResponseEntity.ok().body(orderId.toString());
    }

    @PostMapping("/start_payment")
    public ResponseEntity<String> payment(@RequestParam UUID orderId) {
        seatSagaFacade.startPayment(orderId);
        return ResponseEntity.ok("Event was sent");
    }

    @PostMapping("/accepted_payment")
    public ResponseEntity<String> accepted_payment(@RequestParam UUID orderId) {
        seatSagaFacade.acceptedPayment(orderId);
        return ResponseEntity.ok("Event was sent");
    }

    @PostMapping("/rejected_payment")
    public ResponseEntity<String> rejected_payment(@RequestParam UUID orderId) {
        seatSagaFacade.rejectedPayment(orderId);
        return ResponseEntity.ok("Event was sent");
    }

}
