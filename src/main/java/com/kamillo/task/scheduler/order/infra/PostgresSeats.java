package com.kamillo.task.scheduler.infrastructure.seats;

import com.kamillo.task.scheduler.order.infra.PostgresOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Setter
@Table(name = "Seats")
public class PostgresSeats {

    @Id
    @GeneratedValue
    private long id;

    private long row;

    private long number;

    private boolean free;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    private PostgresOrder order;

    public void setOrder(PostgresOrder order) {
        this.order = order;
    }

}
