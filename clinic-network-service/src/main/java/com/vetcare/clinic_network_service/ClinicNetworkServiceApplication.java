package com.vetcare.network;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

@EnableDiscoveryClient
@SpringBootApplication
public class ClinicNetworkServiceApplication {

    private static final Logger log = LoggerFactory.getLogger(ClinicNetworkServiceApplication.class);

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(ClinicNetworkServiceApplication.class, args);
        Environment env = context.getEnvironment();
        
        String serviceName = env.getProperty("spring.application.name", "Clinic-Network-Service");
        log.info(">>>>> {} started successfully on port: {} <<<<<", 
                 serviceName, 
                 env.getProperty("local.server.port"));
    }
}
