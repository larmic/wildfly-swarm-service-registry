package de.larmic.serviceregistry.core;

import de.larmic.serviceregistry.model.ApplicationEntity;

import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import java.util.Date;
import java.util.List;

@Stateless
public class ApplicationServiceCore {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public ApplicationEntity createApplication(final String name, String serverName, int port) {
        final ApplicationEntity application = new ApplicationEntity();
        application.setName(name);
        application.setLastActive(new Date());
        application.setRegistrationTime(new Date());
        application.setServerName(serverName);
        application.setPort(port);

        em.persist(application);

        return application;
    }

    @Transactional
    public ApplicationEntity keepAlive(final long applicationId) {
        final ApplicationEntity application = em.find(ApplicationEntity.class, applicationId);
        application.setLastActive(new Date());
        return application;
    }

    @Asynchronous
    @Transactional
    public void pingApplication(final long applicationId) {
        final ApplicationEntity application = em.find(ApplicationEntity.class, applicationId);
        final String applicationPingUrl = String.format("http://%s:%d/", application.getServerName(), application.getPort());

        System.out.println("ping application " + applicationPingUrl);

        final javax.ws.rs.client.Client client = ClientBuilder.newClient();
        final WebTarget target = client.target(applicationPingUrl);
        try {
            target.request().get();
        } catch (ProcessingException e) {
            System.out.println(String.format("application %s seems to be offline. It will be removed from registry.", applicationPingUrl));
            em.remove(application);
        }
    }

    public List<ApplicationEntity> findAllApplications() {
        return em.createNamedQuery("Application.findAll", ApplicationEntity.class).getResultList();
    }

    public List<ApplicationEntity> findApplications(final String name) {
        final CriteriaBuilder cb = em.getCriteriaBuilder();
        final CriteriaQuery<ApplicationEntity> query = cb.createQuery(ApplicationEntity.class);
        final Root<ApplicationEntity> root = query.from(ApplicationEntity.class);
        query.select(root)
                .where(cb.equal(root.get("name"), name))
                .orderBy(cb.asc(root.get("serverName")));

        return em.createQuery(query).getResultList();
    }

    public List<ApplicationEntity> findApplications(final String name, final String serverName) {
        final CriteriaBuilder cb = em.getCriteriaBuilder();
        final CriteriaQuery<ApplicationEntity> query = cb.createQuery(ApplicationEntity.class);
        final Root<ApplicationEntity> root = query.from(ApplicationEntity.class);
        query.select(root)
                .where(cb.and(
                        cb.equal(root.get("name"), name),
                        cb.equal(root.get("serverName"), serverName)))
                .orderBy(cb.asc(root.get("port")));

        return em.createQuery(query).getResultList();
    }

    public ApplicationEntity findApplication(final String name, final String serverName, final int port) {
        final CriteriaBuilder cb = em.getCriteriaBuilder();
        final CriteriaQuery<ApplicationEntity> query = cb.createQuery(ApplicationEntity.class);
        final Root<ApplicationEntity> root = query.from(ApplicationEntity.class);
        query.select(root)
                .where(cb.and(
                        cb.equal(root.get("name"), name),
                        cb.equal(root.get("serverName"), serverName),
                        cb.equal(root.get("port"), port)))
                .orderBy(cb.asc(root.get("serverName")));

        final List<ApplicationEntity> applications = em.createQuery(query).getResultList();
        return applications.isEmpty() ? null : applications.get(0);
    }
}
