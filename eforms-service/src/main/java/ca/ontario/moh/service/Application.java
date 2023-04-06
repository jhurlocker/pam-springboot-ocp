package ca.ontario.moh.service;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.drools.persistence.jpa.marshaller.JPAPlaceholderResolverStrategy;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages={"ca.ontario.moh.service", "ca.ontario.moh.models"})
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean(name = "JPAPlaceholderResolverStrategy")
    public JPAPlaceholderResolverStrategy jpaMarshaller(EntityManagerFactory emf) {
        //EntityManagerFactory emfMOH = Persistence.createEntityManagerFactory("moh-jpa");
        return new JPAPlaceholderResolverStrategy(emf);
    }
}