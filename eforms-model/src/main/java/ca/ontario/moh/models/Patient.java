package ca.ontario.moh.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
public class Patient implements java.io.Serializable {

    static final long serialVersionUID = 1L;

@Id
@GeneratedValue
private Long id;
    
private String firstName = "";
private String lastName = "";
private String fullName = "";

public Patient() {
    super();
}

public Patient(String firstName, String lastName, String fullName) {
    super();
    this.firstName = firstName;
    this.lastName = lastName;
    this.fullName = fullName;
}

@Override
public String toString() {
    return String.format(
        "Model[id=%d, firstName='%s', lastName='%s',fullName='%s']",
        id, firstName, lastName, fullName);
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