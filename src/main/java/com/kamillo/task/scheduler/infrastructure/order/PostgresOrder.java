package com.kamillo.task.scheduler.infrastructure.order;

import com.kamillo.task.scheduler.domain.saga.SagaSeatEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import java.util.UUID;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Setter
@Table(name = "order_saga")
public class PostgresOrder {

    @Id
    @GeneratedValue
    private long id;

    private UUID orderId;

    private SagaSeatEnum orderState;

}
