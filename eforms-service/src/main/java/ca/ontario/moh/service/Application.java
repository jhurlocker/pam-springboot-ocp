package ca.ontario.moh.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages={"ca.ontario.moh.service", "ca.ontario.moh.models"})
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}