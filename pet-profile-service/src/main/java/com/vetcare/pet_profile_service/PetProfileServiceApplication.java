package com.vetcare.pet_profile_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class PetProfileServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PetProfileServiceApplication.class, args);
	}

}
