package de.larmic.serviceregistry.swarm;

import de.larmic.serviceregistry.Client;
import de.larmic.serviceregistry.resource.ClientResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.wildfly.swarm.container.Container;
import org.wildfly.swarm.jaxrs.JAXRSArchive;

public class Main {

    public static void main(String[] args) throws Exception {
        Container container = new Container();

        container.start();

        JAXRSArchive deployment = ShrinkWrap.create(JAXRSArchive.class);
        deployment.addClass(Client.class);
        deployment.addClass(ClientResource.class);
        deployment.addAllDependencies();

        container.deploy(deployment);
    }

}
