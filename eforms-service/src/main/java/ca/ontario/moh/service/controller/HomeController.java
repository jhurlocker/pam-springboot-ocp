package ca.ontario.moh.service.controller;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ca.ontario.moh.models.Patient;

import org.jbpm.services.api.ProcessService;
 
@RestController
@RequestMapping("/api")
public class HomeController {

    @Autowired
    ProcessService ps;

    @GetMapping("/hello")
	public String hello() {
		return "Hello World RESTful with Spring Boot";
	}
    
    @GetMapping("/addpatient")
    public String addPatient(){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("moh-jpa");
		EntityManager entityManager = emf.createEntityManager();
		entityManager.getTransaction().begin();

        System.out.println("--- ABOUT TO ADD PATIENT --- ");
        Patient p = new Patient("John", "Hurlocker", "JRH");

        entityManager.persist(p);
		entityManager.getTransaction().commit();
        System.out.println("--- PATIENT --- " + p.toString());
        return "--- PATIENT --- " + p.toString();
    }
}
