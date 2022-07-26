package com.kamillo.task.scheduler.domain.scheduler;

import com.kamillo.task.scheduler.infrastructure.scheduler.HelloJob;
import com.kamillo.task.scheduler.infrastructure.scheduler.JobsData;
import com.kamillo.task.scheduler.infrastructure.scheduler.TriggersData;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SchedulerServiceFacade {

    private final SchedulerService quartzSchedulerService;

    public SchedulerServiceFacade(SchedulerService quartzSchedulerService) {
        this.quartzSchedulerService = quartzSchedulerService;
    }

    public boolean schedule() {
        int totalCount = 5;
        JobsData jobsData = new JobsData("Job name", totalCount);
        TriggersData triggersData = new TriggersData(3, totalCount - 1, false);
        return quartzSchedulerService.schedule(HelloJob.class, jobsData, triggersData);
    }

    public List<JobsData> getAllJobData() {
        return quartzSchedulerService.getJobData();
    }

    public Optional<JobsData> getRunningJob(String jobClassName) {
        return quartzSchedulerService.getRunningJob(jobClassName);
    }

    public boolean deleteTimer(String timerId) {
        return quartzSchedulerService.deleteTimer(timerId);
    }

}
