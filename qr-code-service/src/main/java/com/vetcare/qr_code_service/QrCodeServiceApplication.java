package com.vetcare.qr_code_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class QrCodeServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(QrCodeServiceApplication.class, args);
	}

}
