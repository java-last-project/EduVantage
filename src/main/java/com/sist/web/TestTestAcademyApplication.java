package com.sist.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class TestTestAcademyApplication {

	public static void main(String[] args) {
		SpringApplication.run(TestTestAcademyApplication.class, args);
	}

}
