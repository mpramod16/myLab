package com.ahstores.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.ahstores.app.service.QueueListenerService;

@SpringBootApplication
@ComponentScan
@EnableAutoConfiguration
@EnableScheduling
public class AHStoreApplication extends SpringBootServletInitializer{

    private static final Logger LOG = LoggerFactory.getLogger(AHStoreApplication.class);
        
	public static void main(String[] args) {
		
        ApplicationContext applicationContext = SpringApplication.run(AHStoreApplication.class, args);
        for (String name : applicationContext.getBeanDefinitionNames()) {
			System.out.println(name);
		}
        
	}
        
        protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(AHStoreApplication.class);
	}
}
