package com.vetcare.employee;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.core.env.Environment;

import java.util.Arrays;

@EnableDiscoveryClient
@SpringBootApplication
public class EmployeeServiceApplication {

    private static final Logger log = LoggerFactory.getLogger(EmployeeServiceApplication.class);
    private final Environment env;

    public EmployeeServiceApplication(Environment env) {
        this.env = env;
    }

  
    @PostConstruct
    public void initApplication() {
        log.info("Running with Spring profiles: {}", Arrays.toString(env.getActiveProfiles()));
    }

    public static void main(String[] args) {
        SpringApplication.run(EmployeeServiceApplication.class, args);
    }
}
