package io.fabric8.testing.controller;

import io.fabric8.kubernetes.api.model.KubernetesResourceList;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.MixedOperation;
import io.fabric8.kubernetes.client.dsl.Resource;
import io.fabric8.testing.model.v1alpha1.Book;
import io.fabric8.testing.model.v1alpha1.BookIssueRequest;
import io.fabric8.testing.model.v1alpha1.BookIssueRequestList;
import io.fabric8.testing.model.v1alpha1.BookList;
import io.fabric8.testing.model.v1alpha1.DoneableBook;
import io.fabric8.testing.model.v1alpha1.DoneableBookIssueRequest;
import io.quarkus.runtime.StartupEvent;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Named;

@ApplicationScoped
public class BookOperator {
    @Inject
    KubernetesClient k8sClient;

    @Inject
    MixedOperation<Book, BookList, DoneableBook, Resource<Book, DoneableBook>> bookClient;

    @Inject
    MixedOperation<BookIssueRequest, BookIssueRequestList, DoneableBookIssueRequest, Resource<BookIssueRequest, DoneableBookIssueRequest>> bookIssueRequestClient;

    @Inject
    BookWatcher bookWatcher;

    @Inject
    BookIssueRequestWatcher bookIssueRequestWatcher;

    @Named("namespace")
    String namespace;

    public void onStart(@Observes StartupEvent event) {
        // Watch All BookIssueRequests
        bookIssueRequestClient.inNamespace(namespace).watch(bookIssueRequestWatcher);

        // Watch All Books
        bookClient.inNamespace(namespace).watch(bookWatcher);
    }
}
