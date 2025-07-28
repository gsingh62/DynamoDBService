### Introduction
This package contains both a REST implementation of 
the dynamoDB get() and put() APIs and a GRPC implementation.

### Testing

### Rest APIs on kubernetes pods.
I have tested the REST APIs by deploying to 3 kubernetes pods, that
replicate over a HttpConnection amongst themselves. Actually,
there is a leader node and the other 3 replicate from it in this design

How I did the Kubernetes deployments and testing

eval $(minikube docker-env)

mvn clean package -DskipTests
docker build -t kv-store:latest .

kubectl delete pod -l app=kv-store
kubectl get pods -l app=kv-store -w


kubectl port-forward pod/kv-store-0 8080:8080
curl -X POST "http://localhost:8080/kv?key=foo&value=bar"

kubectl exec -it kv-store-1 -- cat /data/kv.json
kubectl exec -it kv-store-2 -- cat /data/kv.json

## GRPC APIs.

In order to pivot to GRPC APIs, since I am overloading this package
with both the Spring Boot plugin for the REST APIs and the GRPC code.
I needed to comment out the plugin code for spring-boot-maven-plugin
before I compile the package, so that the GRPC files would not be part of
SpringBoot fat jar. If it is part of the SpringBoot fat jar, you get errors like below:

Ran locally like this

```
mvn compile exec:java -Dexec.mainClass="com.grpc.DynamoDBServer"
```

Then tested successfully like this -created the dynamoDB records
foo:bar and gayatri:singh and saw that I could retrieve those results
and that they were persisted in kv.json file
```
gayatrisingh@GayatrisMacBook kubernetes-service % grpcurl -plaintext \
  -import-path src/main/proto \
  -proto dynamodb.proto \
  -d '{"key":"foo", "value":"bar"}' \
  localhost:9090 dynamodb.DynamoDB/put

{}
gayatrisingh@GayatrisMacBook kubernetes-service % ls
deployment.yaml	Dockerfile	k8s		kv.json		META-INF	pom.xml		README.md	src		target
gayatrisingh@GayatrisMacBook kubernetes-service % cat kv.json 
{"foo":"bar"}%                                                                                                                                  gayatrisingh@GayatrisMacBook kubernetes-service % grpcurl -plaintext \
  -import-path src/main/proto \
  -proto dynamodb.proto \
  -d '{"key":"gayatri", "value":"singh"}' \
  localhost:9090 dynamodb.DynamoDB/put

{}
gayatrisingh@GayatrisMacBook kubernetes-service % cat kv.json         
{"foo":"bar","gayatri":"singh"}%                                                                                                                gayatrisingh@GayatrisMacBook kubernetes-service % grpcurl -plaintext \
  -import-path src/main/proto \
  -proto dynamodb.proto \
  -d '{"key":"gayatri"}' \                 
  localhost:9090 dynamodb.DynamoDB/get

{
  "value": "singh"
}
gayatrisingh@GayatrisMacBook kubernetes-service % grpcurl -plaintext \
  -import-path src/main/proto \
  -proto dynamodb.proto \
  -d '{"key":"foo"}' \    
  localhost:9090 dynamodb.DynamoDB/get

{
  "value": "bar"
}

```

```
gayatrisingh@GayatrisMacBook kubernetes-service % java -cp target/kv-store-0.0.1-SNAPSHOT.jar com.grpc.DynamoDBServer

Error: Could not find or load main class com.grpc.DynamoDBServer
Caused by: java.lang.ClassNotFoundException: com.grpc.DynamoDBServer
```

```
<!--      <plugin>-->
<!--        <groupId>org.springframework.boot</groupId>-->
<!--        <artifactId>spring-boot-maven-plugin</artifactId>-->
<!--        <version>${spring.boot.version}</version>-->
<!--        <executions>-->
<!--          <execution>-->
<!--            <goals>-->
<!--              <goal>repackage</goal>-->
<!--            </goals>-->
<!--          </execution>-->
<!--        </executions>-->
<!--      </plugin>-->
```