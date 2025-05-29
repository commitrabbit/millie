package com.millie;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MillieApplication {

	public static void main(String[] args) {
		System.out.println("Starting Millie Application...");
		SpringApplication.run(MillieApplication.class, args);
	}
}
