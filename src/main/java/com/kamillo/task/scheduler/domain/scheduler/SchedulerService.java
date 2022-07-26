package com.kamillo.task.scheduler.domain.scheduler;

import com.kamillo.task.scheduler.infrastructure.scheduler.HelloJob;
import com.kamillo.task.scheduler.infrastructure.scheduler.JobsData;
import com.kamillo.task.scheduler.infrastructure.scheduler.TriggersData;

import java.util.List;
import java.util.Optional;

public interface SchedulerService {
    boolean schedule(Class<HelloJob> helloJobClass, JobsData jobsData, TriggersData triggersData);

    List<JobsData> getJobData();

    Optional<JobsData> getRunningJob(String jobClassName);

    boolean deleteTimer(String timerId);
}
