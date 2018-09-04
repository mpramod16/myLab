package com.tekhealthapi.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SpringBootMongoDbApplication extends SpringBootServletInitializer{

    private static final Logger LOG = LoggerFactory.getLogger(SpringBootMongoDbApplication.class);
    
	public static void main(String[] args) {
		SpringApplication.run(SpringBootMongoDbApplication.class, args);
	}
        
        protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(SpringBootMongoDbApplication.class);
	}
}
