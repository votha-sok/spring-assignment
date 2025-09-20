package com.study.springbootassignment;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
//@OpenAPIDefinition(
//		info = @Info(
//				title = "My API",
//				version = "1.0",
//				description = "Spring Boot REST API documentation"
//		)
//)
@SpringBootApplication(scanBasePackages = "com.study.springbootassignment")
public class SpringBootAssignmentApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootAssignmentApplication.class, args);
	}

}
