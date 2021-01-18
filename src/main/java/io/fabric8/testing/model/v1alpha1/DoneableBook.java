package io.fabric8.testing.model.v1alpha1;

import io.fabric8.kubernetes.api.builder.Function;
import io.fabric8.kubernetes.client.CustomResourceDoneable;

public class DoneableBook extends CustomResourceDoneable<Book> {
    public DoneableBook(Book resource, Function<Book, Book> function) {
        super(resource, function);
    }
}
