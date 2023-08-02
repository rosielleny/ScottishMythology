package com.faction.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.faction")
@EnableJpaRepositories(basePackages = "com.faction.dao")
@EntityScan("com.faction.entity")
public class FactionApplication {

	public static void main(String[] args) {
		SpringApplication.run(FactionApplication.class, args);
	}

}
