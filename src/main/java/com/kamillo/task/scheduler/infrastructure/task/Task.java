package com.kamillo.task.scheduler.infrastructure.task;

import com.kamillo.task.scheduler.infrastructure.order.PostgresOrder;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import jakarta.persistence.*;
import javax.validation.constraints.Max;
import java.time.OffsetTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Setter
@Table(name = "task")
public class Task {

    @Id
    @GeneratedValue
    private long id;

    @Column(name = "created_date", nullable = false, updatable = false)
    @CreatedDate
    private long createdDate;

    @Column(name = "modified_date")
    @LastModifiedDate
    private long modifiedDate;

    private TaskType type;

    private TaskStatus status;

    @Column(name = "createdAt")
    private OffsetTime createdAt;

    public void setOrder(PostgresOrder order) {
        this.order = order;
    }

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    private PostgresOrder order;

}
