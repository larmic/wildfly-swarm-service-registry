package de.larmic.serviceregistry.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/")
public class ClientResource {

    @GET
    @Produces("text/plain")
    public String ping() {
        System.out.println("Ping received");
        return "I am alive!";
    }

}
