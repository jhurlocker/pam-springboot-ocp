package ca.ontario.moh.service.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ca.ontario.moh.models.PatientApplication;
import ca.ontario.moh.models.Provider;
import ca.ontario.moh.models.Model;
import ca.ontario.moh.models.Patient;

//import org.jbpm.runtime.manager.api.qualifiers.Task;
import org.jbpm.services.api.ProcessService;
import org.jbpm.services.api.UserTaskService;
import org.jbpm.casemgmt.api.CaseService;
import org.jbpm.casemgmt.api.model.instance.CaseInstance;
import org.kie.api.task.TaskService;
import org.kie.api.task.model.Status;
//import org.kie.server.services.taskassigning.core.model.Task;
import org.kie.api.task.model.Task;
import org.kie.api.runtime.process.CaseData;
import org.jbpm.services.api.RuntimeDataService;
 
@RestController
@RequestMapping("/api")
public class HomeController {

    @Autowired
    ProcessService processService;

    @Autowired
    UserTaskService userTaskService;

    @Autowired
    RuntimeDataService runtimeDataService;

    @Autowired
    CaseService caseService;

    //CaseData casedata;

    Map<String, Object> processVariables;

    //orderhardware process Id
    Long ohId = 0L;

    private Map<String, Object> params = new HashMap<String, Object>();

    private PatientApplication patientApp = new PatientApplication();

    private Model model = new Model("J", "Hurlocker", "JH");

    private CaseInstance globalCaseInstance;

    @GetMapping("/hello")
	public String hello() {
		return "Hello World RESTful with Spring Boot";
	}

    private String appUser;

    public String getAppUser() {
        return appUser;
    }

    public void setAppUser(String appUser) {
        this.appUser = appUser;
    }

    private String appPass;

    public String getAppPass() {
        return appPass;
    }

    public void setAppPass(String appPass) {
        this.appPass = appPass;
    }
    
    private String appUrl;

    public String getAppUrl() {
        return appUrl;
    }

    public void setAppUrl(String appUrl) {
        this.appUrl = appUrl;
    }

    @GetMapping("/addpatient")
    public String addPatient(){

        System.out.println("DB URL: " + this.getAppUrl());
        System.out.println("DB USER: " + this.getAppUser());
        System.out.println("DB PASS: " + this.getAppPass());
        Map<String, Object> properties = new HashMap<>();
        properties.put("javax.persistence.jdbc.url", this.getAppUrl());
        properties.put("javax.persistence.jdbc.user", this.getAppUser());
        properties.put("javax.persistence.jdbc.password", this.getAppPass());
       
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("moh-jpa", properties);
        
		EntityManager entityManager = emf.createEntityManager();
		entityManager.getTransaction().begin();

        System.out.println("--- ABOUT TO ADD PATIENT --- ");
        Patient p = new Patient("John", "Hurlocker", "JRH");

        entityManager.persist(p);
		entityManager.getTransaction().commit();
        
        System.out.println("--- PATIENT --- " + p.toString());
        return "--- PATIENT --- " + p.toString();
    }
    
    //STARTS THE moh_process
    @GetMapping("/startProcess")
    public String startProcess(){
        System.out.println("DB URL: " + this.getAppUrl());
        System.out.println("DB USER: " + this.getAppUser());
        System.out.println("DB PASS: " + this.getAppPass());
        Map<String, Object> properties = new HashMap<>();
        properties.put("javax.persistence.jdbc.url", this.getAppUrl());
        properties.put("javax.persistence.jdbc.user", this.getAppUser());
        properties.put("javax.persistence.jdbc.password", this.getAppPass());
       
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("moh-jpa", properties);

        //EntityManagerFactory emf = Persistence.createEntityManagerFactory("moh-jpa");
		EntityManager entityManager = emf.createEntityManager();
		entityManager.getTransaction().begin();

        System.out.println("--- ABOUT TO ADD PATIENT --- ");

        patientApp.setFirstName("John");
        patientApp.setLastName("H");
        
        
        entityManager.persist(patientApp);
        // entityManager.flush();
        System.out.println("---- PATIENT APP ID AFTER FLUSH --- " + patientApp.getId());
       
        this.params.put("patientApp", patientApp);
        this.params.put("model", model);

        long pid = processService.startProcess("eforms-kjar-container1", "moh_process", params);
        entityManager.persist(patientApp);
		entityManager.getTransaction().commit();
        //emf.close();
        return "MOH process started. PID:\n\t{}" + pid;

    }
    
    //STARTS THE moh_process
    @GetMapping("/startSimpleProcess")
    public String startSimpleProcess(){

        Patient p = new Patient();
        p.setFirstName("John");
        p.setLastName("H");

        Map<String, Object> parameters = new HashMap<String, Object>();
        
        parameters.put("patient", p);

        long pid = processService.startProcess("eforms-kjar-container1", "simple_process", parameters);

        return "MOH process started. PID:\n\t{}" + pid;

    }
    //
    //STARTS THE order hardware process
    @GetMapping("/orderHardwareProcess")
    public String ordertHardwareProcess(){

        Patient p = new Patient();
        p.setFirstName("John");
        p.setLastName("H");

        Map<String, Object> parameters = new HashMap<String, Object>();
        
        parameters.put("patient", p);

        ohId = processService.startProcess("eforms-kjar-container1", "itorders.orderhardware", parameters);

	return ohId.toString();

    }

    @GetMapping("/orderHardwareCase")
    public String orderHardwareCase(){
	String Message = "";
	    // using our precreated container eforms-kjar-container1
	    // itorders.orderhardware is the name of the case ID and process ID
	Message += caseService.startCase("eforms-kjar-container1", "itorders.orderhardware");
	System.out.println(Message);

	globalCaseInstance = caseService.getCaseInstance("itorders.orderhardware");
	System.out.println(globalCaseInstance.toString());

        Message += "\n";
	Message += globalCaseInstance.toString();

	return Message;	
    }

    //Creates and sets case as global case variable from ITordersHardware
    @GetMapping("/orderHardwareCaseStatus")
    public String orderHardwareCaseStatus(){
	String Message = "";

	System.out.println("_______ORDER_HARDWARE_STATUS_______");
	System.out.println(globalCaseInstance.toString());

        Message += "\n";
	Message += globalCaseInstance.toString();

	return Message;	
    }

    //Milestone Triggers
    //order placed
    @GetMapping("/orderPlacedTrigger")
    public String orderPlacedTrigger(){
	String Message = "";

	processVariables = processService.getProcessInstanceVariables(ohId);
        caseService.triggerAdHocFragment(globalCaseInstance.getCaseId(), "Milestone 1: Order placed", processVariables.replace("ordered", false, true));
	Message += "-----order placed milestone triggered------";

	Message += processVariables.toString();
	System.out.println(Message);
	return Message;
    }

    //order shipped
    @GetMapping("/orderShippedTrigger")
    public String orderShippedTrigger(){
	String Message = "";

	processVariables = processService.getProcessInstanceVariables(ohId);
        caseService.triggerAdHocFragment(globalCaseInstance.getCaseId(), "Milestone 2: Order shipped", processVariables.replace("shipped", false, true));
	Message += "-----order shipped milestone triggered------";

	Message += processVariables.toString();
	System.out.println(Message);
	return Message;
    }

    //order delivered
    @GetMapping("/orderDeliveredTrigger")
    public String orderDeliveredTrigger(){
	String Message = "";

	processVariables = processService.getProcessInstanceVariables(ohId);
        caseService.triggerAdHocFragment(globalCaseInstance.getCaseId(), "Milestone 3: Order delivered", processVariables.replace("shipped", false, true));
	Message += "-----order delievered milestone triggered------";

	Message += processVariables.toString();
	System.out.println(Message);
	return Message;
    }

    //STARTS THE moh_process
    @GetMapping("/startKSProcess")
    public String startKSProcess(){
        // System.out.println("DB URL: " + this.getAppUrl());
        // System.out.println("DB USER: " + this.getAppUser());
        // System.out.println("DB PASS: " + this.getAppPass());
        // Map<String, Object> properties = new HashMap<>();
        // properties.put("javax.persistence.jdbc.url", this.getAppUrl());
        // properties.put("javax.persistence.jdbc.user", this.getAppUser());
        // properties.put("javax.persistence.jdbc.password", this.getAppPass());
        
        // EntityManagerFactory emf = Persistence.createEntityManagerFactory("moh-jpa", properties);

        // //EntityManagerFactory emf = Persistence.createEntityManagerFactory("moh-jpa");
        // EntityManager entityManager = emf.createEntityManager();
        // entityManager.getTransaction().begin();

        // System.out.println("--- ABOUT TO ADD PATIENT --- ");

        // patientApp.setFirstName("John");
        // patientApp.setLastName("H");
        
        
        // entityManager.persist(patientApp);
        // // entityManager.flush();
        // System.out.println("---- PATIENT APP ID AFTER FLUSH --- " + patientApp.getId());
        
        // this.params.put("patientApp", patientApp);
        // this.params.put("model", model);

        long pid = processService.startProcess("eforms-kjar-container1", "mohks_process");
        //entityManager.persist(patientApp);
        //entityManager.getTransaction().commit();
        //emf.close();
        return "MOH process started. PID:\n\t{}" + pid;

    }

    //STARTS A TASKS GIVEN A PROCESS INSTANCE ID (pid)
    @PutMapping("/startTask/{pid}")
    public String startTask(@PathVariable(value = "pid") Long pid){

        List<Long> taskList = runtimeDataService.getTasksByProcessInstanceId(pid);
        Long taskId = taskList.get(0);
        
         
        //Task task = userService.getTask(taskId);
        Status taskStatus = userTaskService.getTask(taskId).getTaskData().getStatus();
        System.out.println("---- TASK STATUS ----" + taskStatus.name());
        System.out.println("---- TASK ID ----" + userTaskService.getTask(taskId).getId());
        //Task task = taskService.getTaskById(taskId).getTaskData().getActivationTime();
        if (taskStatus.name().equals(Status.Ready.name())) {
            userTaskService.claim(taskId.longValue(), "kieserver");
            userTaskService.start(taskId.longValue(), "kieserver");
        } 
        else if (taskStatus.name().equals(Status.Reserved.name())) {
            userTaskService.start(taskId.longValue(), "kieserver");
        }

        return "-- STARTED TASK ID --- " + taskId;
    }

    //COMPLETES A TASK GIVEN A PROCESS INSTANCE ID (pid)
    @PutMapping("/completeTask/{pid}")
    public String completeTask(@PathVariable(value = "pid") Long pid){

        List<Long> taskList = runtimeDataService.getTasksByProcessInstanceId(pid);
        Long taskId = taskList.get(0);
        
        Status taskStatus = userTaskService.getTask(taskId).getTaskData().getStatus();
        System.out.println("---- TASK STATUS ----" + taskStatus.name());
        System.out.println("---- TASK ID ----" + userTaskService.getTask(taskId).getId());

		EntityManager entityManager = this.getEntityManager();
		entityManager.getTransaction().begin();
        String jpql = "SELECT p FROM PatientApplication p WHERE pid = " + pid + "";
        TypedQuery<PatientApplication> query = entityManager.createQuery(jpql, PatientApplication.class);

        PatientApplication patientAppQuery = (PatientApplication) query.getResultList().get(0); 
        entityManager.close();
        this.patientApp.setFirstName(patientAppQuery.getFirstName());
        this.patientApp.setLastName(patientAppQuery.getLastName());
        this.patientApp.setPid(patientAppQuery.getPid());
        System.out.println("-- TASK PATIENT APP FIRST= " + patientApp.getFirstName());
        System.out.println("-- TASK PATIENT APP LAST= " + patientApp.getLastName());
        System.out.println("-- TASK PATIENT PID= " + patientApp.getPid());
        
        Map<String, Object> params = new HashMap<String, Object>();
        
        params.put("patientApp", this.patientApp);
        params.put("model", this.model);

        //userService.complete("eforms-kjar-container1", taskId.longValue(), "kieserver", params);
        
        userTaskService.complete(taskId.longValue(), "kieserver", params);
        System.out.println("--** TASK LAST NAME= " + this.patientApp.getLastName());
        this.updateEntity(patientApp);
        
        return "-- COMPLETED TASK ID --- " + taskId;
    }

    public EntityManager getEntityManager(){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("moh-jpa");
        EntityManager entityManager = emf.createEntityManager();
        return entityManager;
    }

    public void updateEntity(PatientApplication pa){
        EntityManager entityManager = this.getEntityManager();
		entityManager.getTransaction().begin();
        PatientApplication patientAppUpdate = entityManager.find(PatientApplication.class, pa.getId());
        patientAppUpdate.setLastName(pa.getLastName());
        entityManager.getTransaction().commit();
        entityManager.clear();
    }
}
