package de.larmic.wildfly.swarm.serviceregistry;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

@XmlRootElement(name = "Application")
@Entity
@Table(name = "APPLICATION")
@NamedQueries({
        @NamedQuery(name = "Application.findAll", query = "SELECT a FROM ApplicationEntity a")
})
public class ApplicationEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column
    private String name;

    @Column
    private Date registrationTime;

    public ApplicationEntity() {
    }

    public ApplicationEntity(String name, Date registrationTime) {
        this.name = name;
        this.registrationTime = registrationTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getRegistrationTime() {
        return registrationTime;
    }

    public void setRegistrationTime(Date registrationTime) {
        this.registrationTime = registrationTime;
    }
}