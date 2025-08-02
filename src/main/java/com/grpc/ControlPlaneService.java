package com.grpc;

import com.google.protobuf.Empty;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.examples.dynamodb.DynamoDBGrpc;
import io.grpc.examples.dynamodb.Value;
import io.grpc.stub.StreamObserver;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class ControlPlaneService implements DynamoDBGrpc.AsyncService {
    private static final Logger logger = Logger.getLogger(ControlPlaneService.class.getName());
    
    // Map to store gRPC channels to different partition processes
    private final Map<String, DynamoDBGrpc.DynamoDBBlockingStub> partitionStubs;
    private final int numPartitions;
    
    public ControlPlaneService(int numPartitions, String[] partitionHosts, int partitionPort) {
        this.numPartitions = numPartitions;
        this.partitionStubs = new HashMap<>();
        
        // Initialize connections to partition processes
        for (int i = 0; i < numPartitions; i++) {
            String partitionId = "partition-" + i;
            String host = partitionHosts != null && i < partitionHosts.length ? 
                         partitionHosts[i] : "localhost";
            int port = partitionPort + i; // Each partition on different port
            
            ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port)
                    .usePlaintext()
                    .build();
            
            DynamoDBGrpc.DynamoDBBlockingStub stub = DynamoDBGrpc.newBlockingStub(channel);
            partitionStubs.put(partitionId, stub);
            
            logger.info("Initialized connection to partition: " + partitionId + " at " + host + ":" + port);
        }
    }
    
    @Override
    public void put(io.grpc.examples.dynamodb.KeyValue request,
                    StreamObserver<Empty> responseObserver) {
        try {
            String key = request.getKey();
            
            // Determine which partition this key belongs to
            String partitionId = getPartitionForKey(key);
            logger.info("Routing PUT request for key '" + key + "' to partition: " + partitionId);
            
            // Get the appropriate partition stub
            DynamoDBGrpc.DynamoDBBlockingStub partitionStub = partitionStubs.get(partitionId);
            if (partitionStub == null) {
                logger.severe("Partition stub not found: " + partitionId);
                responseObserver.onError(new RuntimeException("Partition not found: " + partitionId));
                return;
            }
            
            // Forward the request to the appropriate partition process
            partitionStub.put(request);
            
            responseObserver.onNext(Empty.getDefaultInstance());
            responseObserver.onCompleted();
            
        } catch (Exception e) {
            logger.severe("Error in PUT operation: " + e.getMessage());
            responseObserver.onError(e);
        }
    }
    
    @Override
    public void get(io.grpc.examples.dynamodb.Key request,
                    StreamObserver<Value> responseObserver) {
        try {
            String key = request.getKey();
            
            // Determine which partition this key belongs to
            String partitionId = getPartitionForKey(key);
            logger.info("Routing GET request for key '" + key + "' to partition: " + partitionId);
            
            // Get the appropriate partition stub
            DynamoDBGrpc.DynamoDBBlockingStub partitionStub = partitionStubs.get(partitionId);
            if (partitionStub == null) {
                logger.severe("Partition stub not found: " + partitionId);
                responseObserver.onError(new RuntimeException("Partition not found: " + partitionId));
                return;
            }
            
            // Forward the request to the appropriate partition process
            Value response = partitionStub.get(request);
            
            responseObserver.onNext(response);
            responseObserver.onCompleted();
            
        } catch (Exception e) {
            logger.severe("Error in GET operation: " + e.getMessage());
            responseObserver.onError(e);
        }
    }
    
    /**
     * Determines which partition a key belongs to using consistent hashing
     */
    private String getPartitionForKey(String key) {
        int hash = hashKey(key);
        int partitionIndex = Math.abs(hash) % numPartitions;
        return "partition-" + partitionIndex;
    }
    
    /**
     * Creates a hash of the key using SHA-256
     */
    private int hashKey(String key) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(key.getBytes());
            
            // Convert first 4 bytes to integer
            int hash = 0;
            for (int i = 0; i < 4 && i < hashBytes.length; i++) {
                hash = (hash << 8) | (hashBytes[i] & 0xFF);
            }
            
            return hash;
        } catch (NoSuchAlgorithmException e) {
            logger.severe("SHA-256 algorithm not available: " + e.getMessage());
            // Fallback to simple hash
            return key.hashCode();
        }
    }
    
    /**
     * Get partition information for monitoring
     */
    public String getPartitionInfo(String key) {
        return getPartitionForKey(key);
    }
    
    /**
     * Shutdown all partition connections
     */
    public void shutdown() {
        for (Map.Entry<String, DynamoDBGrpc.DynamoDBBlockingStub> entry : partitionStubs.entrySet()) {
            try {
                ManagedChannel channel = (ManagedChannel) entry.getValue().getChannel();
                channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
                logger.info("Shutdown connection to partition: " + entry.getKey());
            } catch (InterruptedException e) {
                logger.severe("Failed to shutdown partition connection: " + e.getMessage());
            }
        }
    }
} 