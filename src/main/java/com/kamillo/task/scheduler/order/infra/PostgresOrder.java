package com.kamillo.task.scheduler.infrastructure.order;

import com.kamillo.task.scheduler.domain.saga.SagaSeatEnum;
import com.kamillo.task.scheduler.infrastructure.seats.Seats;
import com.kamillo.task.scheduler.infrastructure.task.Task;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;

import java.util.UUID;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Setter
@Table(name = "PostgresOrder")
public class PostgresOrder {

    @Id
    @GeneratedValue
    private long id;

    private UUID orderId;

    private SagaSeatEnum orderState;

    @OneToOne(mappedBy = "order", fetch = FetchType.EAGER)
    private Task task;

    @OneToOne(mappedBy = "order", fetch = FetchType.EAGER)
    private Seats seats;

    public void setTask(Task task) {
        this.task = task;
        task.setOrder(this);
    }

    public void setSeats(Seats seats) {
        this.seats = seats;
        seats.setOrder(this);
    }
}
