package com.vetcare.medication_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class MedicationServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MedicationServiceApplication.class, args);
	}

}
