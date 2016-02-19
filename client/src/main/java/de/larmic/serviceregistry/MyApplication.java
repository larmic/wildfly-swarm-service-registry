package de.larmic.serviceregistry;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Application;

@ApplicationPath("/")
public class MyApplication extends Application {

    public static final String APPLICATION_NAME = "client1";

    public MyApplication() {
        final javax.ws.rs.client.Client client = ClientBuilder.newClient();
        final WebTarget target = client.target("http://localhost:8084/registry/application/{name}");
        target.resolveTemplate("name", APPLICATION_NAME).request().put(Entity.text(APPLICATION_NAME));
    }
}
