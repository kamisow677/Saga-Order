package com.kamillo.task.scheduler.order.infra;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
public class NoSuchOrderException extends RuntimeException {

    private final UUID orderId;

}
