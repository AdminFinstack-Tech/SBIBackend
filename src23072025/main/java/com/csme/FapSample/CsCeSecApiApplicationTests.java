package com.csme.FapSample;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.csme.csmeapi.fin.config.FinConfig;
import com.csme.csmeapi.fin.config.FinUtil;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * The main class for the CSCE Security API application tests.
 */
@SpringBootApplication
@EnableSwagger2
@EnableScheduling
@ComponentScan(basePackages = {
        "com.csme.FapSample",
        "com.csme.csmeapi.api",
        "com.csme.csmeapi.api.controller",
        "com.csme.csmeapi.Configuration",
        "com.csme.csmeapi.fin.config",
        "com.csme.csmeapi.fin.models",
        "com.csme.csmeapi.fin.services"
})
public class CsCeSecApiApplicationTests extends SpringBootServletInitializer {
    /**
     * The main method to start the application.
     *
     * @param args The command line arguments.
     * @throws Exception If an error occurs during application startup.
     */
	
	@Autowired
    private FinConfig finConfig;

    @Autowired
    private FinUtil finUtil;
    
    public static void main(String[] args) throws Exception {
        (new SpringApplication(new Class[] { CsCeSecApiApplicationTests.class })).run(args);
    }
}
