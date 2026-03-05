package com.vetcare.network;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

import java.util.Optional;

@EnableDiscoveryClient
@SpringBootApplication
public class ClinicNetworkServiceApplication {

    private static final Logger log = LoggerFactory.getLogger(ClinicNetworkServiceApplication.class);

    public static void main(String[] args) {
        ConfigurableApplicationContext context = new SpringApplicationBuilder(ClinicNetworkServiceApplication.class)
                .logStartupInfo(true)
                .run(args);

        logServiceDetails(context.getEnvironment());
    }

    private static void logServiceDetails(Environment env) {
        String name = env.getProperty("spring.application.name");
        String port = env.getProperty("server.port");
        String profile = Optional.of(env.getActiveProfiles()).map(p -> p.length > 0 ? p[0] : "default").orElse("default");

        log.info("""
            
            ----------------------------------------------------------
            \tApplication '{}' is running!
            \tPort: \t\t{}
            \tProfile(s): \t{}
            ----------------------------------------------------------
            """, name, port, profile);
    }
}

