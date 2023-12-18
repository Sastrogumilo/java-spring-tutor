package com.wahook_java.wahook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.wahook_java.wahook.Service.LoggerService;

import jakarta.annotation.PostConstruct;


@SpringBootApplication
public class WahookApplication {
	

	private LoggerService logger;

	public WahookApplication(LoggerService logger){
		this.logger = logger;
	}

	@PostConstruct
	public void init(){
		logger.log("Test SpringBoot berjalan ", "", "", "", "");
	}
	public static void main(String[] args) {
		
		SpringApplication.run(WahookApplication.class, args);

	}

}
