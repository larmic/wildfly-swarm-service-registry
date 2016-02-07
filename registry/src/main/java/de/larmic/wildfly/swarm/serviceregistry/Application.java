package de.larmic.wildfly.swarm.serviceregistry;

import java.util.Date;

public class Application {

    private final String name;
    private final Date registrationTime;

    public Application(String name, Date registrationTime) {
        this.name = name;
        this.registrationTime = registrationTime;
    }

    public String getName() {
        return name;
    }

    public Date getRegistrationTime() {
        return registrationTime;
    }
}
