package com.kamillo.task.scheduler.infrastructure.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface GeneratedOrderRepo extends JpaRepository<PostgresOrder, Long> {

    @Query("SELECT o FROM PostgresOrder o LEFT JOIN FETCH o.task WHERE o.orderId = ?1")
    Optional<PostgresOrder> getByOrderID(UUID orderId);

    @Query("SELECT o FROM PostgresOrder o JOIN FETCH o.task WHERE o.orderId = ?1")
    Optional<PostgresOrder> getByOrderID2(UUID orderId);

    @Query("SELECT o FROM PostgresOrder o")
    Iterable<PostgresOrder> getByOrderID3();

}
