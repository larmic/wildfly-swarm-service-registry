package de.larmic.wildfly.swarm.serviceregistry;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Application;

@ApplicationPath("/")
public class MyApplication extends Application {

    public MyApplication() {
        final javax.ws.rs.client.Client client = ClientBuilder.newClient();
        final WebTarget target = client.target("http://localhost:8080/registry/application/{name}");
        target.resolveTemplate("name", "client-application-1").request().put(Entity.text("de"));
    }
}
