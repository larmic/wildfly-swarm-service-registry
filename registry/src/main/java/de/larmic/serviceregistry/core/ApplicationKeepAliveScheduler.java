package de.larmic.serviceregistry.core;


import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.inject.Inject;

@Singleton
public class ApplicationKeepAliveScheduler {

    @Inject
    private ApplicationServiceCore applicationServiceCore;

    @Schedule(second = "*/3", minute = "*", hour = "*", persistent = false)
    public void doWork() {
        System.out.println("Start schedule");
        applicationServiceCore.findAllApplications().stream()
                .forEach(application -> applicationServiceCore.pingApplication(application.getId()));
        System.out.println("Finished schedule");
    }

}
