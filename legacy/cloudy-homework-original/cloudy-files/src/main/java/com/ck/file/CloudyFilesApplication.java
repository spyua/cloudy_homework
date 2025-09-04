package com.ck.file;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = {"com.ck.*"})
@ComponentScan(basePackages = {"com.ck.*"})
@EntityScan("com.ck.*")
public class CloudyFilesApplication {

	public static void main(String[] args) {
		SpringApplication.run(CloudyFilesApplication.class, args);
	}

}
