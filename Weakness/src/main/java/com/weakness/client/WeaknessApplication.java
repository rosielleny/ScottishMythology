package com.weakness.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.weakness")
@EnableJpaRepositories(basePackages = "com.weakness.dao")
@EntityScan("com.weakness.entity")
public class WeaknessApplication {

	public static void main(String[] args) {
		SpringApplication.run(WeaknessApplication.class, args);
	}

}
