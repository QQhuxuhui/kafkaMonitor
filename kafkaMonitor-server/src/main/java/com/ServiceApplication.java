package com;

import com.mashape.unirest.http.Unirest;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ServiceApplication {

	public static void main(String[] args) {
		Unirest.setTimeouts(5000,5000);
		SpringApplication.run(ServiceApplication.class, args);
	}

}
