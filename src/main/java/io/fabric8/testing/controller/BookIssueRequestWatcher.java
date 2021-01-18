package io.fabric8.testing.controller;

import io.fabric8.kubernetes.client.KubernetesClientException;
import io.fabric8.kubernetes.client.Watcher;
import io.fabric8.kubernetes.client.dsl.MixedOperation;
import io.fabric8.kubernetes.client.dsl.Resource;
import io.fabric8.testing.model.v1alpha1.Book;
import io.fabric8.testing.model.v1alpha1.BookIssueRequest;
import io.fabric8.testing.model.v1alpha1.BookIssueRequestList;
import io.fabric8.testing.model.v1alpha1.BookList;
import io.fabric8.testing.model.v1alpha1.BookStatus;
import io.fabric8.testing.model.v1alpha1.DoneableBook;
import io.fabric8.testing.model.v1alpha1.DoneableBookIssueRequest;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.Collections;

@ApplicationScoped
public class BookIssueRequestWatcher implements Watcher<BookIssueRequest> {
    private static final Logger LOG = Logger.getLogger(BookIssueRequestWatcher.class);

    @Inject
    MixedOperation<Book, BookList, DoneableBook, Resource<Book, DoneableBook>> bookClient;

    @Inject
    MixedOperation<BookIssueRequest, BookIssueRequestList, DoneableBookIssueRequest, Resource<BookIssueRequest, DoneableBookIssueRequest>> bookIssueRequest;

    @Named("namespace")
    String namespace;

    @Override
    public void eventReceived(Action action, BookIssueRequest bookIssueRequest) {
        LOG.info("Event received " + action + " received for BookIssueRequest " + bookIssueRequest.getMetadata().getName());
        handleBookIssueRequestEvent(action, bookIssueRequest);
    }

    @Override
    public void onClose(KubernetesClientException e) {
        if (e != null) {
            LOG.info("on close");
            e.printStackTrace();
            System.exit(-1);
        }
    }

    private void handleBookIssueRequestEvent(Action action, BookIssueRequest bookIssueRequest) {
        switch (action) {
            case ADDED:
                onAdd(bookIssueRequest);
                break;
            case MODIFIED:
                onUpdate(bookIssueRequest);
                break;
            case DELETED:
                onDelete(bookIssueRequest);
                break;
            default:
                LOG.warn("Unindentified event occured");
        }
    }

    private void onAdd(BookIssueRequest bookIssueRequest) {
        LOG.info("onAdd : " + bookIssueRequest.getMetadata().getName());
        issueBook(bookIssueRequest);
    }

    private void onUpdate(BookIssueRequest bookIssueRequest) {
        LOG.info("onUpdate : " + bookIssueRequest.getMetadata().getName());
        checkUpdatedBookIssueRequest(bookIssueRequest);
    }

    private void onDelete(BookIssueRequest bookIssueRequest) {
        LOG.info("onDelete : " + bookIssueRequest.getMetadata().getName());
        returnBook(bookIssueRequest);
    }

    private void issueBook(BookIssueRequest bookIssueRequest) {
        if (bookIssueRequest != null) {
            Book book = getBookFromServer(bookIssueRequest.getSpec().getName());
            if (book != null) {
                issueBookTo(book, bookIssueRequest.getSpec().getTo());
            }
        }
    }

    private void returnBook(BookIssueRequest bookIssueRequest) {
        if (bookIssueRequest != null) {
            Book book = getBookFromServer(bookIssueRequest.getSpec().getName());
            if (book != null) {
                updatedBookStatus(book, false, "");
                removeBookIssuedLabel(book);
            }
        }
    }

    private void checkUpdatedBookIssueRequest(BookIssueRequest bookIssueRequest) {
        Book book = getBookFromServer(bookIssueRequest.getSpec().getName());
        if (book != null) {
            if (book.getStatus() == null) {
                issueBook(bookIssueRequest);
            } else if (!book.getStatus().getIssuedto().equals(bookIssueRequest.getSpec().getTo())) {
                updatedBookStatus(book, true, bookIssueRequest.getSpec().getTo());
            }
        } else {
            LOG.warn("Book with name " + bookIssueRequest.getSpec().getName() + " not found.");
        }
    }

    private Book getBookFromServer(String name) {
        Book book = bookClient.inNamespace(namespace).withName(name).get();
        if (book == null) {
            LOG.warn("Book with name " + name + " not found.");
        }
        return book;
    }

    private Book issueBookTo(Book book, String issueTo) {
        book = updatedBookStatus(book, true, issueTo);
        book = updateBookLabels(book);
        return book;
    }

    private Book updateBookLabels(Book book) {
        book.getMetadata().setLabels(Collections.singletonMap("issued", "true"));
        return bookClient.inNamespace(namespace).withName(book.getMetadata().getName()).patch(book);
    }

    private Book removeBookIssuedLabel(Book book) {
        book.getMetadata().setLabels(Collections.singletonMap("issued", "false"));
        return bookClient.inNamespace(namespace).withName(book.getMetadata().getName()).patch(book);
    }

    private Book updatedBookStatus(Book book, boolean isIssued, String issueTo) {
        book.setStatus(createBookStatus(isIssued, issueTo));

        book = bookClient.inNamespace(namespace).withName(book.getMetadata().getName()).updateStatus(book);
        return book;
    }

    private BookStatus createBookStatus(boolean isIssued, String issuedTo) {
        BookStatus bookStatus = new BookStatus();
        bookStatus.setIssued(isIssued);
        bookStatus.setIssuedto(issuedTo);
        return bookStatus;
    }
}
