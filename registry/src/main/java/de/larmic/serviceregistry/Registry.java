package de.larmic.serviceregistry;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath(Registry.APPLICATION_PATH)
public class Registry extends Application {

    public static final String APPLICATION_PATH = "/registry";

    public Registry() {
    }

}
