package com.vetcare.calculation_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class CalculationServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CalculationServiceApplication.class, args);
	}

}
