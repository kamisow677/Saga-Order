package com.kamillo.task.scheduler.domain.scheduler;

import org.quartz.JobDataMap;
import org.quartz.SchedulerException;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Optional;

@Service
public class SchedulerServiceFacade {

    @Autowired
    SchedulerJobFactory schedulerJobFactory;
    @Autowired
    SchedulerTriggerFactory schedulerTriggerFactory;
    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;

    public TriggerKey schedulePaymentFinishedJob() {
        // define the job and tie it to our PaymentFinishedJob class
        JobDataMap data = new JobDataMap();

        JobDetailFactoryBean jdfb = schedulerJobFactory.job(PaymentFinishedJob.class, data, PaymentFinishedJob.class.getName());
        CronTriggerFactoryBean stfb =
                schedulerTriggerFactory.jobTrigger(jdfb.getObject(),
                        "0/5 * * * * ?",
                        "CronTriggerFactoryBean");

        // Tell quartz to schedule the job using our trigger
        try {
            schedulerFactoryBean.getScheduler()
                    .scheduleJob(jdfb.getObject(), stfb.getObject());
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return stfb.getObject().getKey();
    }

    public boolean unschedulePaymentFinishedJob(TriggerKey triggerKey) {
        // Tell quartz to schedule the job using our trigger
        try {
            schedulerFactoryBean.getScheduler()
                    .unscheduleJob(triggerKey);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return true;
    }


    public Optional<String> getRunningJob(String jobClassName) {
        return Optional.of("");
    }

    @PostConstruct
    public void setup() {
    }

    @PreDestroy
    public void clean() {
    }


}
