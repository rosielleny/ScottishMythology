package com.symbol.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.symbol")
@EnableJpaRepositories(basePackages = "com.symbol.dao")
@EntityScan("com.symbol.entity")
public class SymbolApplication {

	public static void main(String[] args) {
		SpringApplication.run(SymbolApplication.class, args);
	}

}
