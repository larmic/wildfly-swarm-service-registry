package de.larmic.wildfly.swarm.serviceregistry;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class Client {

    @PostConstruct
    public void init() {
        System.out.println("dindong");
    }

}
