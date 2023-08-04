package com.gender.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.gender")
@EnableJpaRepositories(basePackages = "com.gender.dao")
@EntityScan("com.gender.entity")
public class GenderApplication {

	public static void main(String[] args) {
		SpringApplication.run(GenderApplication.class, args);
	}

}
