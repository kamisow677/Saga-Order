package com.kamillo.task.scheduler.infrastructure.task;

public enum TaskType {
    // If payment started payment 20 seconds ago and there is no message from Payment Service make seat free
    CHECK_PAYMENT_FINISHED("CHECK_PAYMENT_FINISHED");

    private final String jobName;

    TaskType(String check_payment_finished) {
        jobName = check_payment_finished;
    }

    public String getJobName() {
        return jobName;
    }
}
