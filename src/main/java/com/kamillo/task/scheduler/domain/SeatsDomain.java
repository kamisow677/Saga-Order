package com.kamillo.task.scheduler.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SeatsDomain {

    private long id;

    private long row;

    private long number;

    private boolean free;

    public SeatsDomain(long id, long row, long number, boolean free) {
        this.id = id;
        this.row = row;
        this.number = number;
        this.free = free;
    }
}
