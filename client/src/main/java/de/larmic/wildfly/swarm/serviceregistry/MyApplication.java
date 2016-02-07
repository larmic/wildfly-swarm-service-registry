package de.larmic.wildfly.swarm.serviceregistry;

import org.jboss.resteasy.client.ClientRequest;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.client.*;
import javax.ws.rs.core.Application;

@ApplicationPath("/")
public class MyApplication extends Application {

    public MyApplication() {
        System.out.println("klaps");
        final javax.ws.rs.client.Client client = ClientBuilder.newClient();
        final WebTarget target = client.target("http://localhost:8080/registry/application/{name}");
        target.resolveTemplate("name", "de").request().put(Entity.text("de"));
        ClientRequest request = new ClientRequest("http://localhost:8080/registry/application/{name}");

        request.pathParameter("name", "demoding");

        try {
            System.out.println(request.put());
        } catch (Exception e) {
            e.printStackTrace();
        }

        //final ResteasyClient client = new ResteasyClientBuilder().build();
        //final ResteasyWebTarget target = client.target("http://localhost:8080/registry/application/test");
        //target.request().put(Entity.entity());
    }
}
