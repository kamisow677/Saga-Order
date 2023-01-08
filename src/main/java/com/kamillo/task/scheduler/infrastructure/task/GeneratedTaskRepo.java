package com.kamillo.task.scheduler.infrastructure.task;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GeneratedTaskRepo extends JpaRepository<Task, Long> {

    @Query("SELECT t FROM Task t WHERE t.type = ?1")
    List<Task> findAllByType(TaskType type);

}
