package io.fabric8.testing.model.v1alpha1;

import io.fabric8.kubernetes.api.model.Namespaced;
import io.fabric8.kubernetes.client.CustomResource;

public class Book extends CustomResource implements Namespaced {
    private BookSpec spec;
    private BookStatus status;

    public BookSpec getSpec() {
        return spec;
    }

    public void setSpec(BookSpec spec) {
        this.spec = spec;
    }

    public BookStatus getStatus() {
        return status;
    }

    public void setStatus(BookStatus status) {
        this.status = status;
    }
}
