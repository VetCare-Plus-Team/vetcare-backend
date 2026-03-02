package com.vetcare.clinic_network_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class ClinicNetworkServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClinicNetworkServiceApplication.class, args);
	}

}
