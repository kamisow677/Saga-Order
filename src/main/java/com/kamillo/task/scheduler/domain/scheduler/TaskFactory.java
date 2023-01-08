package com.kamillo.task.scheduler.domain.scheduler;

import com.kamillo.task.scheduler.infrastructure.task.Task;
import com.kamillo.task.scheduler.infrastructure.task.TaskType;

import java.util.UUID;

public interface TaskFactory {

    Task createTask(UUID orderId, TaskType checkPaymentFinished);

}
