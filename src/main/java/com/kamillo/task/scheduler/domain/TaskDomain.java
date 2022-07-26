package com.kamillo.task.scheduler.domain;


public class TaskDomain {
    private TaskStatus status = TaskStatus.NEW;
}

enum TaskStatus {
    NEW, IN_PROGRESS, DONE, FAILED
}