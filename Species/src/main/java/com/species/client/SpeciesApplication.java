package com.species.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.species")
@EnableJpaRepositories(basePackages = "com.species.dao")
@EntityScan("com.species.entity")
public class SpeciesApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpeciesApplication.class, args);
	}

}
