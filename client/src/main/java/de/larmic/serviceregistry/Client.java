package de.larmic.serviceregistry;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Application;
import java.net.Inet4Address;
import java.net.UnknownHostException;

@ApplicationPath("/")
public class Client extends Application {

    public static final String APPLICATION_NAME = "simpleClient";

    public Client() throws UnknownHostException {
        final String port = System.getProperty("swarm.http.port");
        final String hostAddress = Inet4Address.getLocalHost().getHostAddress();

        final javax.ws.rs.client.Client client = ClientBuilder.newClient();
        final WebTarget target = client.target("http://localhost:8084/registry/application/{name}/{serverName}/{port}");
        target.resolveTemplate("name", APPLICATION_NAME)
                .resolveTemplate("serverName", hostAddress)
                .resolveTemplate("port", port)
                .request().put(Entity.text(APPLICATION_NAME));
    }
}
