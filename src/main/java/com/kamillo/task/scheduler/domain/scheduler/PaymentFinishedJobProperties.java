package com.kamillo.task.scheduler.domain.scheduler;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "paymentfinished")
public class PaymentFinishedJobProperties {
    long timeToFinish;
}