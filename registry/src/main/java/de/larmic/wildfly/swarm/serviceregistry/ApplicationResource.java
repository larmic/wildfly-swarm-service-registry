package de.larmic.wildfly.swarm.serviceregistry;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.ws.rs.*;
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
    public Response registerApplication(@PathParam("name") final String name) {
        final ApplicationEntity application = new ApplicationEntity(name, new Date());
        em.persist(application);
        final String response = String.format("Application %s at %s registered", application.getName(), application.getRegistrationTime());
        System.out.println(name);
        return Response.status(200).entity(response).build();
    }
}
