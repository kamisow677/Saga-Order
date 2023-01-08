package com.kamillo.task.scheduler.infrastructure.order;

import lombok.Data;

import java.util.UUID;

@Data
public class NoSuchOrderException extends RuntimeException {

    private final UUID orderId;

}
