package com.vetcare.notifications;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


@EnableDiscoveryClient
@SpringBootApplication
public class NotificationServiceApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(NotificationServiceApplication.class);
       
        app.setBannerMode(org.springframework.boot.Banner.Mode.OFF);
        app.run(args);
    }
}