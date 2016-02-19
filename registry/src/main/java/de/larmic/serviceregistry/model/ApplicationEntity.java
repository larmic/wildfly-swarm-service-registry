package de.larmic.serviceregistry.model;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.Date;

@XmlRootElement(name = "Application")
@Entity
@Table(name = "APPLICATION", uniqueConstraints = @UniqueConstraint(columnNames = {"name", "serverName", "port"}))
@NamedQueries({
        @NamedQuery(name = "Application.findAll", query = "SELECT a FROM ApplicationEntity a")
})
public class ApplicationEntity implements Serializable {

    @XmlTransient
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false)
    private String name;

    @Column
    private Date registrationTime;

    @Column
    private Date lastActive;

    @Column(nullable = false)
    private String serverName;

    @Column(nullable = false)
    private int port;

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public Date getLastActive() {
        return lastActive;
    }

    public void setLastActive(Date lastActive) {
        this.lastActive = lastActive;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String ip) {
        this.serverName = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
