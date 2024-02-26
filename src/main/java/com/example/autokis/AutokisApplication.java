package com.example.autokis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class AutokisApplication {

	public static void main(String[] args) {
		SpringApplication.run(AutokisApplication.class, args);
	}

}
