package com.autoservice.ordenes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class OrdenesServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrdenesServiceApplication.class, args);
	}
}