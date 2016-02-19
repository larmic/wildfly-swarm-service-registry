package de.larmic.wildfly.swarm.serviceregistry;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import java.util.Date;
import java.util.List;

@Path("/application")
public class ApplicationResource {

    @PersistenceContext
    private EntityManager em;

    @GET
    @Produces({"application/json", "application/xml"})
    public Response get() {
        final List<ApplicationEntity> applications = em.createNamedQuery("Application.findAll", ApplicationEntity.class).getResultList();
        final GenericEntity<List<ApplicationEntity>> listGenericEntity = new GenericEntity<List<ApplicationEntity>>(applications) {};
        return Response.ok(listGenericEntity).build();
    }

    @PUT
    @Path("/{name}")
    @Transactional
    public Response registerApplication(@PathParam("name") final String name, @Context HttpServletRequest request) {
        // TODO check if application already exist
        final ApplicationEntity application = createApplication(name, request);
        em.persist(application);
        final String response = String.format("Application %s registered at %s", application.getName(), application.getRegistrationTime());
        return Response.status(200).entity(response).build();
    }

    private ApplicationEntity createApplication(final  String name, final HttpServletRequest request) {
        final ApplicationEntity application = new ApplicationEntity();
        application.setName(name);
        application.setLastActive(new Date());
        application.setRegistrationTime(new Date());

        // TODO set correct server name and port
        application.setServerName(request.getServerName());
        application.setPort(request.getLocalPort());
        return application;
    }
}
