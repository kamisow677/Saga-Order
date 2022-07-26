package com.kamillo.task.scheduler.infrastructure.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface GeneratedOrderRepo extends JpaRepository<PostgresOrder, Long> {

    @Query("SELECT o FROM PostgresOrder o WHERE o.orderId = ?1")
    Optional<PostgresOrder> getByOrderID(UUID orderId);

}
