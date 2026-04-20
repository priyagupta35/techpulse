package com.techpulse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TechpulseApplication {

	public static void main(String[] args) {
		SpringApplication.run(TechpulseApplication.class, args);
	}

}
