package de.larmic.serviceregistry.core;

import de.larmic.serviceregistry.model.ApplicationEntity;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Named
@RequestScoped
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
    public void keepAlive(final long applicationId) {
        em.find(ApplicationEntity.class, applicationId).setLastActive(new Date());
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
