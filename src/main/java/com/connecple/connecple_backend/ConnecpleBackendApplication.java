package com.connecple.connecple_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ConnecpleBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConnecpleBackendApplication.class, args);
	}

}
