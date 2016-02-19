package de.larmic.wildfly.swarm.serviceregistry;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath(MyApplication.APPLICATION_PATH)
public class MyApplication extends Application {

    public static final String APPLICATION_PATH = "/registry";

    public MyApplication() {
    }

}
