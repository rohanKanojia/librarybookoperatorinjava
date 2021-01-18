package io.fabric8.testing.model.v1alpha1;

import io.fabric8.kubernetes.api.model.Namespaced;
import io.fabric8.kubernetes.client.CustomResource;
import io.fabric8.kubernetes.model.annotation.Group;
import io.fabric8.kubernetes.model.annotation.Version;

@Version("v1alpha1")
@Group("testing.fabric8.io")
public class BookIssueRequest extends CustomResource<BookIssueRequestSpec, BookIssueRequestStatus> implements Namespaced {
}
