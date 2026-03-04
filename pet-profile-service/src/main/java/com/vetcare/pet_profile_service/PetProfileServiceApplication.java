package com.vetcare.pet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

@EnableDiscoveryClient
@SpringBootApplication
public class PetProfileServiceApplication {

    private static final Logger log = LoggerFactory.getLogger(PetProfileServiceApplication.class);

    public static void main(String[] args) {
        ConfigurableApplicationContext context = new SpringApplicationBuilder(PetProfileServiceApplication.class)
                .logStartupInfo(true)
                .run(args);

        Environment env = context.getEnvironment();
        log.info("""
            ----------------------------------------------------------
            \tPet Profile Service '{}' is active!
            \tExternal API Documentation: http://localhost:{}/swagger-ui.html
            ----------------------------------------------------------
            """, env.getProperty("spring.application.name"), env.getProperty("server.port"));
    }
}