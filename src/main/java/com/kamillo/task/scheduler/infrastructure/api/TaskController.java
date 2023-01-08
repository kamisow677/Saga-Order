package com.kamillo.task.scheduler.infrastructure.api;

import com.kamillo.task.scheduler.domain.scheduler.SchedulerServiceFacade;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/task")
public class TaskController {

    private final SchedulerServiceFacade schedulerServiceFacade;

    public TaskController(SchedulerServiceFacade schedulerServiceFacade) {
        this.schedulerServiceFacade = schedulerServiceFacade;
    }

    @GetMapping("/schedule")
    public ResponseEntity runTestScheduler() {
            return ResponseEntity.ok("Scheduled");
    }

}
