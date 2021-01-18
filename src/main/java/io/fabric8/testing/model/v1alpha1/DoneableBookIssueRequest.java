package io.fabric8.testing.model.v1alpha1;

import io.fabric8.kubernetes.api.builder.Function;
import io.fabric8.kubernetes.client.CustomResourceDoneable;

public class DoneableBookIssueRequest extends CustomResourceDoneable<BookIssueRequest> {
    public DoneableBookIssueRequest(BookIssueRequest resource, Function<BookIssueRequest, BookIssueRequest> function) {
        super(resource, function);
    }
}
