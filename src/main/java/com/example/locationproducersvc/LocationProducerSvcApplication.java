package com.example.locationproducersvc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class LocationProducerSvcApplication {

	public static void main(String[] args) {
		SpringApplication.run(LocationProducerSvcApplication.class, args);
	}

}
