package com.vetcare.pet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

@EnableDiscoveryClient
@SpringBootApplication
public class PetProfileServiceApplication {

    private static final Logger log = LoggerFactory.getLogger(PetProfileServiceApplication.class);

    public static void main(String[] args) {
        log.info("Starting Pet Profile Service...");
        SpringApplication.run(PetProfileServiceApplication.class, args);
    }

   
    @Bean
    public CommonsRequestLoggingFilter requestLoggingFilter() {
        CommonsRequestLoggingFilter loggingFilter = new CommonsRequestLoggingFilter();
        loggingFilter.setIncludeClientInfo(true);
        loggingFilter.setIncludeQueryString(true);
        loggingFilter.setIncludePayload(true);
        loggingFilter.setMaxPayloadLength(10000);
        return loggingFilter;
    }
}