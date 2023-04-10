package com.kamillo.task.scheduler.infrastructure.seats;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface GeneratedSeatsRepo extends JpaRepository<PostgresSeats, Long> {

    @Query("SELECT s FROM PostgresSeats s WHERE s.row = ?1 AND s.number = ?2")
    Optional<PostgresSeats> findByNumberIdAndRow(long numberId, long rowId);

    @Modifying
    @Query("UPDATE PostgresSeats s " +
            "SET s.free = true " +
            "WHERE s.id in " +
            "( SELECT s.id FROM PostgresSeats s JOIN s.order o WHERE o.orderId = ?1 ) ")
    void freeSeat(UUID orderId);

    @Query("SELECT s FROM PostgresSeats s JOIN s.order o WHERE o.orderId = ?1")
    Optional<PostgresSeats> findByOrderOrderId(UUID orderId);
}
