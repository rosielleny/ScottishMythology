package com.ability.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.ability")
@EnableJpaRepositories(basePackages = "com.ability.dao")
@EntityScan("com.ability.entity")
public class AbilityApplication {

	public static void main(String[] args) {
		SpringApplication.run(AbilityApplication.class, args);
	}

}
