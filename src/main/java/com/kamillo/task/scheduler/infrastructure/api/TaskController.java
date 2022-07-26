package com.kamillo.task.scheduler.infrastructure.api;

import com.kamillo.task.scheduler.infrastructure.scheduler.JobsData;
import com.kamillo.task.scheduler.domain.scheduler.SchedulerServiceFacade;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/task")
public class TaskController {

    private final SchedulerServiceFacade schedulerServiceFacade;

    public TaskController(SchedulerServiceFacade schedulerServiceFacade) {
        this.schedulerServiceFacade = schedulerServiceFacade;
    }

    @GetMapping("/schedule")
    public ResponseEntity runTestScheduler() {
        return schedulerServiceFacade.schedule()
                ? ResponseEntity.ok("Scheduled")
                : ResponseEntity.ok("This job is already scheduled");
    }

    @GetMapping
    public List<JobsData> getAllDataJobs() {
        return schedulerServiceFacade.getAllJobData();
    }

    @DeleteMapping("/{timerId}")
    public ResponseEntity deleteTimer(@PathVariable String timerId) {
        return schedulerServiceFacade.deleteTimer(timerId)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.badRequest().build();
    }

}
