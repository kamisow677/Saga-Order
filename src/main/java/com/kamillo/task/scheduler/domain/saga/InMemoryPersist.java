package com.kamillo.task.scheduler.domain.saga;

import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.StateMachinePersist;

import java.util.HashMap;

public class InMemoryPersist
        implements StateMachinePersist<SagaSeatEnum, SagaSeatEvents, String> {

    private HashMap<String, StateMachineContext<SagaSeatEnum, SagaSeatEvents>> storage
            = new HashMap<>();

    @Override
    public void write(StateMachineContext<SagaSeatEnum, SagaSeatEvents> context,
                      String contextObj) throws Exception {
        storage.put(contextObj, context);
    }

    @Override
    public StateMachineContext<SagaSeatEnum, SagaSeatEvents> read(String contextObj) throws Exception {
        return storage.get(contextObj);
    }
}