package com.example.petify;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
public class petifyApplication {

	public static void main(String[] args) {
		SpringApplication.run(petifyApplication.class, args);
	}

}
