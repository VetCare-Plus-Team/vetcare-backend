package com.vetcare.vaccination_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class VaccinationServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(VaccinationServiceApplication.class, args);
	}

}
