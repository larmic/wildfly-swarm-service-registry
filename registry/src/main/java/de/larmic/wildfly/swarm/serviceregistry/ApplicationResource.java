package de.larmic.wildfly.swarm.serviceregistry;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.*;

@Path("/")
public class ApplicationResource {

    private static final List<Application> applications = Collections.synchronizedList(new ArrayList<Application>());

    @GET
    @Produces({"application/json", "application/xml"})
    @Path("/application")
    public Collection<Application> get() {
        return applications;
    }

    @PUT
    @Path("/application/{name}")
    public Response registerApplication(@PathParam("name") final String name) {
        applications.add(new Application(name, new Date()));
        final String response = "Hello from: " + name;
        System.out.println(name);
        return Response.status(200).entity(response).build();
    }
}