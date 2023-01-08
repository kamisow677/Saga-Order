package com.kamillo.task.scheduler.infrastructure.seats;

import com.kamillo.task.scheduler.infrastructure.order.PostgresOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Setter
@Table(name = "Seats")
public class Seats {

    @Id
    @GeneratedValue
    private long id;

    private long row;

    private long number;

    private boolean free;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    private PostgresOrder order;

    public void setOrder(PostgresOrder order) {
        this.order = order;
    }

}
