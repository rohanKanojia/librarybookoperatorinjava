package io.fabric8.testing.controller;

import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.MixedOperation;
import io.fabric8.kubernetes.client.dsl.Resource;
import io.fabric8.kubernetes.client.dsl.base.CustomResourceDefinitionContext;
import io.fabric8.testing.model.v1alpha1.Book;
import io.fabric8.testing.model.v1alpha1.BookIssueRequest;
import io.fabric8.testing.model.v1alpha1.BookIssueRequestList;
import io.fabric8.testing.model.v1alpha1.BookList;
import io.fabric8.testing.model.v1alpha1.DoneableBook;
import io.fabric8.testing.model.v1alpha1.DoneableBookIssueRequest;
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
    MixedOperation<Book, BookList, DoneableBook, Resource<Book, DoneableBook>> makeBookClient(KubernetesClient kubernetesClient, @Named("bookCrdContext") CustomResourceDefinitionContext bookCrdContext) {
        LOG.info("Initializing Book CustomResource Client");
        return kubernetesClient.customResources(bookCrdContext, Book.class, BookList.class, DoneableBook.class);
    }

    @Produces
    @Singleton
    MixedOperation<BookIssueRequest, BookIssueRequestList, DoneableBookIssueRequest, Resource<BookIssueRequest, DoneableBookIssueRequest>> makeBookIssueRequestClient(KubernetesClient kubernetesClient, @Named("bookIssueRequestCrdContext") CustomResourceDefinitionContext bookIssueRequestCrdContext) {
        LOG.info("Initializing BookIssueRequest CustomResource Client");
        return kubernetesClient.customResources(bookIssueRequestCrdContext, BookIssueRequest.class, BookIssueRequestList.class, DoneableBookIssueRequest.class);
    }

    @Produces
    @Singleton
    @Named("bookCrdContext")
    CustomResourceDefinitionContext bookCRDContext() {
        return new CustomResourceDefinitionContext.Builder()
                .withGroup("testing.fabric8.io")
                .withVersion("v1alpha1")
                .withScope("Namespaced")
                .withKind("Book")
                .withPlural("books")
                .build();
    }

    @Produces
    @Singleton
    @Named("bookIssueRequestCrdContext")
    CustomResourceDefinitionContext bookIssueRequestCRDContext() {
        return new CustomResourceDefinitionContext.Builder()
                .withGroup("testing.fabric8.io")
                .withVersion("v1alpha1")
                .withScope("Namespaced")
                .withKind("BookIssueRequest")
                .withPlural("bookissuerequests")
                .build();
    }

}
