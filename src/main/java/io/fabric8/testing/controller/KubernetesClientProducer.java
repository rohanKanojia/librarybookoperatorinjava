package io.fabric8.testing.controller;

import io.fabric8.kubernetes.api.model.KubernetesResourceList;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.MixedOperation;
import io.fabric8.kubernetes.client.dsl.Resource;
import io.fabric8.testing.model.v1alpha1.Book;
import io.fabric8.testing.model.v1alpha1.BookIssueRequest;
import io.quarkus.arc.profile.IfBuildProfile;
import org.jboss.logging.Logger;

import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.Produces;

public class KubernetesClientProducer {
    private static final Logger LOG = Logger.getLogger(KubernetesClientProducer.class);

    @Produces
    @Singleton
    @Named("namespace")
    String findMyCurrentNamespace(KubernetesClient client) {
        return client.getConfiguration().getNamespace();
    }

    @Produces
    @Singleton
    @IfBuildProfile("prod")
    KubernetesClient makeKubernetesClient() {
        LOG.info("Initializing KubernetesClient");
        return new DefaultKubernetesClient();
    }

    @Produces
    @Singleton
    MixedOperation<Book, KubernetesResourceList<Book>, Resource<Book>> makeBookClient(KubernetesClient kubernetesClient) {
        LOG.info("Initializing Book CustomResource Client");
        return kubernetesClient.customResources(Book.class);
    }

    @Produces
    @Singleton
    MixedOperation<BookIssueRequest, KubernetesResourceList<BookIssueRequest>, Resource<BookIssueRequest>> makeBookIssueRequestClient(KubernetesClient kubernetesClient) {
        LOG.info("Initializing BookIssueRequest CustomResource Client");
        return kubernetesClient.customResources(BookIssueRequest.class);
    }

}
