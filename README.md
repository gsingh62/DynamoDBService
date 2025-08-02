# Key-Value Store Service

This package contains both a REST implementation of the DynamoDB get() and put() APIs and a gRPC implementation, including a distributed control plane service for request routing.

## Features

- **REST APIs**: Traditional HTTP-based key-value operations
- **gRPC APIs**: High-performance gRPC-based key-value operations  
- **Distributed Control Plane**: Routes requests based on key hash to separate partition processes
- **Kubernetes Support**: Ready for deployment on Kubernetes clusters

## Architecture

### Basic gRPC Service
- Single-node key-value store with gRPC interface
- Persistence to JSON files
- Peer replication capabilities

### Distributed Control Plane Service
- **Control Plane Server**: Routes requests to partition processes (port 9090)
- **Partition Servers**: Separate processes handling specific partitions (ports 9091, 9092, 9093)
- **Consistent Hashing**: SHA-256 based key distribution across partitions
- **DynamoDB-like Design**: Similar to how DynamoDB partitions work

## Quick Start

### 1. Build the Project
```bash
mvn clean package
```

### 2. Run Basic gRPC Service
```bash
# Start the basic gRPC server
java -cp "target/classes:target/dependency/*" com.grpc.DynamoDBServer

# Test with grpcurl
grpcurl -plaintext \
  -import-path src/main/proto \
  -proto dynamodb.proto \
  -d '{"key":"foo", "value":"bar"}' \
  localhost:9090 dynamodb.DynamoDB/put

grpcurl -plaintext \
  -import-path src/main/proto \
  -proto dynamodb.proto \
  -d '{"key":"foo"}' \
  localhost:9090 dynamodb.DynamoDB/get
```

### 3. Run Distributed Control Plane Service

#### Option A: Using the provided script
```bash
# Start all servers automatically
./run-distributed-system.sh

# Test the distributed system
./test-client.sh
```

#### Option B: Manual startup
```bash
# Terminal 1 - Partition 0
java -cp "target/classes:target/dependency/*" com.grpc.PartitionServer 9091

# Terminal 2 - Partition 1  
java -cp "target/classes:target/dependency/*" com.grpc.PartitionServer 9092

# Terminal 3 - Partition 2
java -cp "target/classes:target/dependency/*" com.grpc.PartitionServer 9093

# Terminal 4 - Control Plane
java -cp "target/classes:target/dependency/*" com.grpc.ControlPlaneServer 9090 3 9091

# Terminal 5 - Test Client
java -cp "target/classes:target/dependency/*" com.grpc.ControlPlaneTestClient
```

## Distributed System Architecture

```
Client → Control Plane (9090) → Partition Server (9091/9092/9093)
```

### Request Flow
1. Client sends request to Control Plane (port 9090)
2. Control Plane hashes key using SHA-256
3. Control Plane forwards request to appropriate Partition Server
4. Partition Server processes request and returns response
5. Control Plane returns response to client

### Key Distribution
- Keys are distributed using consistent hashing
- Same key always routes to same partition
- Even distribution across partitions
- Configurable number of partitions

## REST APIs on Kubernetes

### Deployment
```bash
eval $(minikube docker-env)
mvn clean package -DskipTests
docker build -t kv-store:latest .

kubectl delete pod -l app=kv-store
kubectl get pods -l app=kv-store -w
```

### Testing
```bash
kubectl port-forward pod/kv-store-0 8080:8080
curl -X POST "http://localhost:8080/kv?key=foo&value=bar"

kubectl exec -it kv-store-1 -- cat /data/kv.json
kubectl exec -it kv-store-2 -- cat /data/kv.json
```

## gRPC APIs

### Basic Service
The basic gRPC service provides:
- PUT operations for storing key-value pairs
- GET operations for retrieving values
- Persistence to JSON files
- Peer replication capabilities

### Distributed Service
The distributed control plane service provides:
- Automatic request routing based on key hash
- Separate partition processes for scalability
- Fault isolation between partitions
- DynamoDB-like architecture

## Configuration

### Maven Configuration
The Spring Boot plugin is commented out to avoid conflicts with gRPC:
```xml
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

### Distributed System Configuration
- **Number of partitions**: Configurable via command line arguments
- **Port allocation**: Control plane on 9090, partitions on 9091-9093
- **Host configuration**: Can specify different hosts for partitions

## Testing

### Basic gRPC Testing
```bash
# Start server
java -cp "target/classes:target/dependency/*" com.grpc.DynamoDBServer

# Test with grpcurl
grpcurl -plaintext \
  -import-path src/main/proto \
  -proto dynamodb.proto \
  -d '{"key":"foo", "value":"bar"}' \
  localhost:9090 dynamodb.DynamoDB/put

grpcurl -plaintext \
  -import-path src/main/proto \
  -proto dynamodb.proto \
  -d '{"key":"foo"}' \
  localhost:9090 dynamodb.DynamoDB/get
```

### Distributed System Testing
```bash
# Use the provided test client
./test-client.sh

# Or test manually with grpcurl
grpcurl -plaintext \
  -import-path src/main/proto \
  -proto dynamodb.proto \
  -d '{"key":"user:123", "value":"John Doe"}' \
  localhost:9090 dynamodb.DynamoDB/put
```

## Troubleshooting

### Port Conflicts
If you get "Address already in use" errors:
```bash
# Check what's using the ports
lsof -i :9090 -i :9091 -i :9092 -i :9093

# Kill processes if needed
kill -9 <PID>
```

### ClassNotFoundException
Make sure to use the correct classpath:
```bash
java -cp "target/classes:target/dependency/*" com.grpc.DynamoDBServer
```

## Next Steps

For production use, consider:
1. **Load Balancing**: Multiple control plane instances
2. **Health Checks**: Monitor partition server health
3. **Replication**: Copy data across partition servers
4. **Persistence**: Save partition data to disk
5. **Monitoring**: Add metrics and observability
6. **Kubernetes Deployment**: Deploy distributed system on Kubernetes

## Documentation

- [Control Plane Service Documentation](CONTROL_PLANE_BASIC_README.md) - Detailed guide for the distributed system
