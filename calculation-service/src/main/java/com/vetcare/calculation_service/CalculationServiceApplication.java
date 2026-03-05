package com.vetcare.calculation_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Main entry point for the Calculation Service Application.
 * 
 * This service handles all calculation-related operations
 * in the VetCare microservice architecture.
 */
@SpringBootApplication
@EnableDiscoveryClient
public class CalculationServiceApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(CalculationServiceApplication.class);
        application.run(args);
    }
}