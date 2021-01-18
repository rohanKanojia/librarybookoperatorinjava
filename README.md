# Simple Kubernetes Operator demonstrating Library Management

This is a simple demo Kubernetes Operator written to mimic Library Management using [Quarkus Fabric8 Kubernetes Client Extension](https://quarkus.io/guides/kubernetes-client). It operates on the following Custom Resources:
- Book
- BookIssueRequest

## Scenarios Considered:
- User creates a BookIssueRequest which updates the requested Book setting it's issued status to `true` and updating issuedTo section in Book's status. 

- On Deleting any `BookIssueRequest`, `Book` is updated again and is marked available for issue(by setting issued status to `false`

- Any update in `BookIssueRequest` object would update specified `Book` resource in case it's different from previous value

Both `Book` and `BookIssueRequest` CustomResources are watched in specified namespace and Operator tries to issue/free any book which is requested via adding/deleting any `BookIssueRequest`.

## How to Build
You can build it as any standard maven project
```shell
mvn clean install
```


