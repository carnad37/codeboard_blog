package com.hhs.codeboard.blog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class CodeboardBlogApplication {

	public static void main(String[] args) {
		SpringApplication.run(CodeboardBlogApplication.class, args);
	}

}
