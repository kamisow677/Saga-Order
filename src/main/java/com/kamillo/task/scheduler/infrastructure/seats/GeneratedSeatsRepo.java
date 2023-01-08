package com.kamillo.task.scheduler.infrastructure.seats;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface GeneratedSeatsRepo extends JpaRepository<Seats, Long> {

    @Query("SELECT s FROM Seats s WHERE s.row = ?1 AND s.number = ?2")
    Optional<Seats> findByNumberIdAndRow(long numberId, long rowId);

    @Query("UPDATE Seats s " +
            "SET s.free = true " +
            "WHERE s.id in " +
            "( SELECT s.id FROM Seats s JOIN s.order o WHERE o.id = ?1 ) ")
    void freeSeat(UUID orderId);
}
