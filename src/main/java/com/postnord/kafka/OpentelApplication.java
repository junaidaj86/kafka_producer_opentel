package com.postnord.kafka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class OpentelApplication {

	public static void main(String[] args) {
		SpringApplication.run(OpentelApplication.class, args);
	}

}
