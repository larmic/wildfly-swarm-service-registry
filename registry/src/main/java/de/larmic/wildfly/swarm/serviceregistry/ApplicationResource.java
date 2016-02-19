package de.larmic.wildfly.swarm.serviceregistry;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import java.util.Date;
import java.util.List;

@Path(ApplicationResource.APPLICATION)
public class ApplicationResource {

    public static final String APPLICATION = "/application";
    @PersistenceContext
    private EntityManager em;

    @GET
    @Produces({"application/json", "application/xml"})
    public Response get() {
        final List<ApplicationEntity> applications = em.createNamedQuery("Application.findAll", ApplicationEntity.class).getResultList();
        final GenericEntity<List<ApplicationEntity>> listGenericEntity = new GenericEntity<List<ApplicationEntity>>(applications) {
        };
        return Response.ok(listGenericEntity).build();
    }

    @PUT
    @Path("/{name}")
    @Transactional
    public Response registerApplication(@PathParam("name") final String name, @Context HttpServletRequest request) {
        ApplicationEntity application = findApplication(name, request.getRemoteHost(), request.getRemotePort());

        if (application == null) {
            application = createApplication(name, request);
            em.persist(application);
        }

        final String response = String.format("Application %s registered at %s", application.getName(), application.getRegistrationTime());
        return Response.status(200).header("location", createApplicationHeaderLocation(application)).entity(response).build();
    }

    private ApplicationEntity findApplication(final String name, final String serverName, final int port) {
        final CriteriaBuilder cb = em.getCriteriaBuilder();
        final CriteriaQuery<ApplicationEntity> query = cb.createQuery(ApplicationEntity.class);
        final Root<ApplicationEntity> root = query.from(ApplicationEntity.class);
        query.select(root)
                .where(cb.and(
                        cb.equal(root.get("name"), name),
                        cb.equal(root.get("serverName"), serverName),
                        cb.equal(root.get("port"), port)))
                .orderBy(cb.asc(root.get("port")));

        final List<ApplicationEntity> applications = em.createQuery(query).getResultList();
        return applications.isEmpty() ? null : applications.get(0);
    }

    private String createApplicationHeaderLocation(final ApplicationEntity application) {
        return String.format("%s%s/%s/%s/%d",
                MyApplication.APPLICATION_PATH,
                ApplicationResource.APPLICATION,
                application.getName(),
                application.getServerName(),
                application.getPort());
    }

    private ApplicationEntity createApplication(final String name, final HttpServletRequest request) {
        final ApplicationEntity application = new ApplicationEntity();
        application.setName(name);
        application.setLastActive(new Date());
        application.setRegistrationTime(new Date());
        application.setServerName(request.getRemoteHost());
        application.setPort(request.getRemotePort());
        return application;
    }
}
