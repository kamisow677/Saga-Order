package com.kamillo.task.scheduler.order.infra;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @Version
    private Integer version;

}
