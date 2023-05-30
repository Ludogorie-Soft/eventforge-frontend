package com.example.EventForgeFrontend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class EventForgeFrontendApplication {

	public static void main(String[] args) {
		SpringApplication.run(EventForgeFrontendApplication.class, args);

	}

}
