package com.vetcare.employee;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.core.env.Environment;

import java.util.Arrays;
import java.util.Collection;

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
        Collection<String> activeProfiles = Arrays.asList(env.getActiveProfiles());
        if (activeProfiles.contains("dev") && activeProfiles.contains("prod")) {
            log.error("Critical Configuration Error: 'dev' and 'prod' profiles are both active!");
        }
        log.info("Employee Service initialized with profiles: {}", activeProfiles);
    }

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(EmployeeServiceApplication.class);
        // Ensure the app shuts down cleanly, closing database connections first
        app.setRegisterShutdownHook(true); 
        app.run(args);
    }
}
