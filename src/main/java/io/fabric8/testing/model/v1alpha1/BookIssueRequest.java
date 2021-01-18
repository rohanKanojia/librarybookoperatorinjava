package io.fabric8.testing.model.v1alpha1;

import io.fabric8.kubernetes.api.model.Namespaced;
import io.fabric8.kubernetes.client.CustomResource;

public class BookIssueRequest extends CustomResource implements Namespaced {
    private BookIssueRequestSpec spec;
    private BookIssueRequestStatus status;

    public BookIssueRequestSpec getSpec() {
        return spec;
    }

    public void setSpec(BookIssueRequestSpec spec) {
        this.spec = spec;
    }

    public BookIssueRequestStatus getStatus() {
        return status;
    }

    public void setStatus(BookIssueRequestStatus status) {
        this.status = status;
    }

}
