package ca.ontario.moh.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@javax.persistence.Entity
@javax.persistence.Table(name = "PatientApplication")
public class PatientApplication implements java.io.Serializable {

static final long serialVersionUID = 1L;

@javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.AUTO, generator = "PATIENT_APP_ID_GENERATOR")
@javax.persistence.Id
@javax.persistence.SequenceGenerator(name = "PATIENT_APP_ID_GENERATOR", sequenceName = "PATIENT_APP_ID_SEQ")
private Long id;
//PROCESS ID
private long pid; 
private String firstName = "";
private String lastName = "";
private String fullName = "";

public PatientApplication() {
    super();
}

public PatientApplication(long pid, String firstName, String lastName, String fullName) {
    super();
    this.pid = pid;
    this.firstName = firstName;
    this.lastName = lastName;
    this.fullName = fullName;
}

@Override
public String toString() {
    return String.format(
        "Model[id=%d, pid=%d, firstName='%s', lastName='%s',fullName='%s']",
        id, pid, firstName, lastName, fullName);
}

public long getPid() {
    return pid;
}

public void setPid(long pid) {
    this.pid = pid;
}

public void setId(Long id) {
    this.id = id;
}
public Long getId() {
    return id;
}
public String getFirstName() {
    return firstName;
}
public void setFirstName(String firstName) {
    this.firstName = firstName;
}
public String getLastName() {
    return lastName;
}
public void setLastName(String lastName) {
    this.lastName = lastName;
}
public String getFullName() {
    return fullName;
}
public void setFullName(String fullName) {
    this.fullName = fullName;
}

}