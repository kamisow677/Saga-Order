package com.kamillo.task.scheduler.order.infra;

import lombok.Data;

import java.util.UUID;

@Data
class NoSuchOrderException extends RuntimeException {

    private final UUID orderId;

}
