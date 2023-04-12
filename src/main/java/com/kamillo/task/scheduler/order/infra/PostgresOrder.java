package com.kamillo.task.scheduler.order.infra;

import com.kamillo.task.scheduler.domain.saga.SagaSeatEnum;
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

    @OneToOne(mappedBy = "order", fetch = FetchType.LAZY)
    private PostgresSeats seats;

    @Version
    private Integer version;

    public void setTask(Task task) {
        this.task = task;
        task.setOrder(this);
    }

    public void setSeats(PostgresSeats seats) {
        if (seats != null) {
            this.seats = seats;
            seats.setOrder(this);
        }
    }

    void deleteSeats() {
        this.seats.setOrder(null);
        this.seats = null;
    }
}
