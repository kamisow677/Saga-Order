package com.kamillo.task.scheduler.infrastructure.task;

public enum TaskStatus {
    // If payment started payment 20 seconds ago and there is no message from Payment Service make seat free
    PENDING("PENDING"), DONE("DONE"), FAILED("FAILED");

    private final String statusName;

    TaskStatus(String statusName) {
        this.statusName = statusName;
    }

    public String getJobName() {
        return statusName;
    }
}
