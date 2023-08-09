package com.myth.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ViewResolver;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.spring5.dialect.SpringStandardDialect;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

@SpringBootApplication(scanBasePackages = "com.myth")
@EnableJpaRepositories(basePackages = "com.myth.dao")
@EntityScan("com.myth.entity")
public class ScottishMythologyApplication {

	public static void main(String[] args) {
		SpringApplication.run(ScottishMythologyApplication.class, args);
	}

	@Bean
	RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Bean
	HttpHeaders headers() {
		return new HttpHeaders();
	}

}
