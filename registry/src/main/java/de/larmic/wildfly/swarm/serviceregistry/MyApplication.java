package de.larmic.wildfly.swarm.serviceregistry;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/registry")
public class MyApplication extends Application {

    public MyApplication() {
    }

}
