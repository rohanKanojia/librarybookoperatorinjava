package io.fabric8.testing.controller;

import io.fabric8.kubernetes.client.KubernetesClientException;
import io.fabric8.kubernetes.client.Watcher;
import io.fabric8.testing.model.v1alpha1.Book;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class BookWatcher implements Watcher<Book> {
    private static final Logger LOG = Logger.getLogger(BookWatcher.class);

    @Override
    public void eventReceived(Action action, Book book) {
        LOG.info("Event received " + action + " received for Book " + book.getMetadata().getName());
        handleBookEvent(action, book);
    }

    @Override
    public void onClose(KubernetesClientException e) {
        if (e != null) {
            LOG.info("on close");
            e.printStackTrace();
            System.exit(-1);
        }
    }

    private void onAdd(Book book) {
        LOG.info("onAdd : " + book.getMetadata().getName());
    }

    private void onUpdate(Book book) {
        LOG.info("onUpdate : " + book.getMetadata().getName());
    }

    private void onDelete(Book book) {
        LOG.info("onDelete : " + book.getMetadata().getName());
    }

    private void handleBookEvent(Action action, Book book) {
        switch (action) {
            case ADDED:
                onAdd(book);
                break;
            case MODIFIED:
                onUpdate(book);
                break;
            case DELETED:
                onDelete(book);
                break;
            default:
                LOG.warn("Unindentified event occured");
        }
    }
}
