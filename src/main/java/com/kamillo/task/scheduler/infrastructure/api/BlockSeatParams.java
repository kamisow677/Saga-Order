package com.kamillo.task.scheduler.infrastructure.api;

import lombok.Data;

@Data
public class BlockSeatParams {
    private final String rowId;
    private final String numberId;

    public BlockSeatParams(String rowId, String numberId) {
        this.rowId = rowId;
        this.numberId = numberId;
    }
}
