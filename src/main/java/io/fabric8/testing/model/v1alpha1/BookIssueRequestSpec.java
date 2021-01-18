package io.fabric8.testing.model.v1alpha1;

public class BookIssueRequestSpec {
    private String to;
    private String name;

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public String toString() {
        return "BookIssueRequestSpec{to=" + to +
                ",name=" + name + "}";
    }
}
