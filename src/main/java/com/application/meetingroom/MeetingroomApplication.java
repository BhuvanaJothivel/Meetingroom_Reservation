package com.application.meetingroom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.time.LocalDateTime;

@SpringBootApplication
@EnableSwagger2
@EnableScheduling
public class MeetingroomApplication {

	public static void main(String[] args) {
		SpringApplication.run(MeetingroomApplication.class, args);
		System.out.println(LocalDateTime.now());
	}

}