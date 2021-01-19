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

## Installing Custom Resource Definitions
As an administrator, you would need to create these CRDs in your Kubernetes cluster:
```shell
kubectl create -f src/main/resources/k8s/crds/book-crd.yaml
kubectl create -f src/main/resources/k8s/crds/bookissuerequest-crd.yaml
```

### How to Run
Make sure that you have access to a Kubernetes Cluster(minikube or crc). You can start your Operator locally using standard Quarkus Maven Plugin goals:
```shell
mvn quarkus:dev
```

### Deploying to Kubernetes using Eclipse JKube
In order to deploy this Operator to Kubernetes, we will be using [Eclipse JKube](https://github.com/eclipse/jkube). First we would need to deploy `Role`, `RoleBinding` and `ServiceAccount` for our Operator to Kubernetes

```shell
kubectl create -f src/main/resources/k8s/role.yaml
kubectl create -f src/main/resources/k8s/rolebinding.yaml
kubectl create -f src/main/resources/k8s/serviceaccount.yaml
```

Then you should be able to deploy the Operator using this command:
```shell
mvn k8s:deploy -PKubernetes
```
