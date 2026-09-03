package com.sist.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
public class EduVantageApplication {

	public static void main(String[] args) {
		SpringApplication.run(EduVantageApplication.class, args);
	}

}
