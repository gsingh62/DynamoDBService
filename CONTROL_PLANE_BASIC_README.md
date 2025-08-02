# Control Plane Service - Distributed Implementation

This is a distributed control plane service that routes requests based on key hash to separate partition processes, similar to how DynamoDB works.

## Architecture

The system consists of:

1. **Control Plane Server**: Routes requests to appropriate partition processes
2. **Partition Servers**: Separate processes that handle requests for specific partitions
3. **Test Client**: Demonstrates the distributed functionality

## How it works

### Distributed Architecture
- **Control Plane**: Runs on port 9090, routes requests to partition processes
- **Partition-0**: Runs on port 9091, handles partition-0 requests
- **Partition-1**: Runs on port 9092, handles partition-1 requests  
- **Partition-2**: Runs on port 9093, handles partition-2 requests

### Request Flow
1. Client sends request to Control Plane (port 9090)
2. Control Plane hashes the key to determine partition
3. Control Plane forwards request to appropriate Partition Server
4. Partition Server processes the request and returns response
5. Control Plane returns response to client

## How to run

### Option 1: Using the provided script (Recommended)
```bash
# This will start all servers automatically
./run-distributed-system.sh
```

### Option 2: Manual startup

1. **Build the project**:
```bash
mvn clean package
```

2. **Start partition servers** (in separate terminals):
```bash
# Terminal 1 - Partition 0
java -cp "target/classes:target/dependency/*" com.grpc.PartitionServer 9091

# Terminal 2 - Partition 1  
java -cp "target/classes:target/dependency/*" com.grpc.PartitionServer 9092

# Terminal 3 - Partition 2
java -cp "target/classes:target/dependency/*" com.grpc.PartitionServer 9093
```

3. **Start control plane server**:
```bash
# Terminal 4 - Control Plane
java -cp "target/classes:target/dependency/*" com.grpc.ControlPlaneServer 9090 3 9091
```

4. **Test with client**:
```bash
# Terminal 5 - Test Client
./test-client.sh
```

## Key Distribution

When you send a request with a key like "user:123":

1. The key is hashed using SHA-256
2. The hash is converted to an integer
3. The integer is modulo'd by the number of partitions (3)
4. The result determines which partition process gets the request

For example:
- "user:123" → partition-0 (port 9091)
- "user:456" → partition-1 (port 9092)
- "product:abc" → partition-2 (port 9093)

## Example Output

When you run the test client, you'll see logs like:

```
PUT request - Key: user:123, Value: John Doe
Routing PUT request for key 'user:123' to partition: partition-0
PUT successful for key: user:123

GET request - Key: user:123
Routing GET request for key 'user:123' to partition: partition-0
GET successful for key: user:123, Value: John Doe
```

And in the partition server logs:
```
Partition Server started, listening on 9091
PUT request received for key: user:123
GET request received for key: user:123
```

## Configuration

You can configure:
- **Number of partitions**: Pass as argument to ControlPlaneServer
- **Control plane port**: First argument to ControlPlaneServer
- **Partition base port**: Third argument to ControlPlaneServer
- **Partition hosts**: Fourth argument (comma-separated) to ControlPlaneServer

## Benefits of this approach

1. **True Distribution**: Each partition runs as a separate process
2. **Scalability**: Can run partitions on different machines
3. **Fault Isolation**: One partition failure doesn't affect others
4. **DynamoDB-like**: Similar to how DynamoDB partitions work
5. **Independent Scaling**: Can scale partitions independently

## Testing

The test client demonstrates:
- Basic PUT/GET operations across partitions
- Key distribution across different partition processes
- Consistent routing (same key always goes to same partition)

## Troubleshooting

If you get `ClassNotFoundException` errors:
1. Make sure you've run `mvn clean package` to build the project
2. The dependencies should be copied to `target/dependency/`
3. Use the provided scripts which include the correct classpath

## Next Steps

For production use, consider:
1. **Load Balancing**: Multiple control plane instances
2. **Health Checks**: Monitor partition server health
3. **Replication**: Copy data across partition servers
4. **Persistence**: Save partition data to disk
5. **Monitoring**: Add metrics and observability 