package com.kamillo.task.scheduler.order.integration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name="test.helpers.enable", havingValue="true", matchIfMissing = false)
public class IntegrationConfiguration {

    @Bean
    public OrderHelper orderHelper() {
        return new OrderHelper();
    }
}
