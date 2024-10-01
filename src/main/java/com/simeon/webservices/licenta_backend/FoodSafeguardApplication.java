package com.simeon.webservices.licenta_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class FoodSafeguardApplication {

	public static void main(String[] args) {
		SpringApplication.run(FoodSafeguardApplication.class, args);
	}

}
