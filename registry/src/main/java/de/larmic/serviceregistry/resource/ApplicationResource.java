package de.larmic.serviceregistry.resource;

import de.larmic.serviceregistry.Registry;
import de.larmic.serviceregistry.core.ApplicationServiceCore;
import de.larmic.serviceregistry.model.ApplicationEntity;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import java.util.List;

@Path(ApplicationResource.APPLICATION)
public class ApplicationResource {

    public static final String APPLICATION = "/application";

    @Inject
    private ApplicationServiceCore applicationServiceCore;

    @GET
    @Produces({"application/json", "application/xml"})
    public Response getAllApplications() {
        final List<ApplicationEntity> applications = applicationServiceCore.findAllApplications();
        final GenericEntity<List<ApplicationEntity>> listGenericEntity = new GenericEntity<List<ApplicationEntity>>(applications) {
        };
        return Response.ok(listGenericEntity).build();
    }

    @GET
    @Produces({"application/json", "application/xml"})
    @Path("/{name}")
    public Response getApplicationsByName(@PathParam("name") final String name) {
        final List<ApplicationEntity> applications = applicationServiceCore.findApplications(name);
        final GenericEntity<List<ApplicationEntity>> listGenericEntity = new GenericEntity<List<ApplicationEntity>>(applications) {
        };
        return Response.ok(listGenericEntity).build();
    }

    @GET
    @Produces({"application/json", "application/xml"})
    @Path("/{name}/{serverName}")
    public Response getApplicationsByNameAndServerName(@PathParam("name") final String name,
                                                       @PathParam("serverName") final String serverName) {
        final List<ApplicationEntity> applications = applicationServiceCore.findApplications(name, serverName);
        final GenericEntity<List<ApplicationEntity>> listGenericEntity = new GenericEntity<List<ApplicationEntity>>(applications) {
        };
        return Response.ok(listGenericEntity).build();
    }

    @GET
    @Produces({"application/json", "application/xml"})
    @Path("/{name}/{serverName}/{port}")
    public Response getApplication(@PathParam("name") final String name,
                                   @PathParam("serverName") final String serverName,
                                   @PathParam("port") final int port) {
        final ApplicationEntity application = applicationServiceCore.findApplication(name, serverName, port);
        return Response.ok(application).build();
    }

    @PUT
    @Path("/{name}")
    public Response registerApplication(@PathParam("name") final String name, @Context HttpServletRequest request) {
        ApplicationEntity application = applicationServiceCore.findApplication(name, request.getRemoteHost(), request.getRemotePort());

        if (application == null) {
            application = applicationServiceCore.createApplication(name, request.getRemoteHost(), request.getRemotePort());
        } else {
            applicationServiceCore.keepAlive(application.getId());
        }

        final String response = String.format("Application %s registered at %s; last active at %s", application.getName(), application.getRegistrationTime(), application.getLastActive());
        return Response.status(200).header("location", createApplicationHeaderLocation(application)).entity(response).build();
    }

    private String createApplicationHeaderLocation(final ApplicationEntity application) {
        return String.format("%s%s/%s/%s/%d",
                Registry.APPLICATION_PATH,
                ApplicationResource.APPLICATION,
                application.getName(),
                application.getServerName(),
                application.getPort());
    }
}
