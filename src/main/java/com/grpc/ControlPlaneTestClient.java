package com.grpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.examples.dynamodb.DynamoDBGrpc;
import io.grpc.examples.dynamodb.Key;
import io.grpc.examples.dynamodb.KeyValue;
import io.grpc.examples.dynamodb.Value;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * Simple test client for the Control Plane Service
 */
public class ControlPlaneTestClient {
    private static final Logger logger = Logger.getLogger(ControlPlaneTestClient.class.getName());
    
    private final ManagedChannel channel;
    private final DynamoDBGrpc.DynamoDBBlockingStub blockingStub;
    
    public ControlPlaneTestClient(String host, int port) {
        channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();
        blockingStub = DynamoDBGrpc.newBlockingStub(channel);
    }
    
    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }
    
    /**
     * Store a key-value pair
     */
    public void put(String key, String value) {
        logger.info("PUT request - Key: " + key + ", Value: " + value);
        
        KeyValue request = KeyValue.newBuilder()
                .setKey(key)
                .setValue(value)
                .build();
        
        try {
            blockingStub.put(request);
            logger.info("PUT successful for key: " + key);
        } catch (Exception e) {
            logger.severe("PUT failed for key " + key + ": " + e.getMessage());
        }
    }
    
    /**
     * Retrieve a value by key
     */
    public String get(String key) {
        logger.info("GET request - Key: " + key);
        
        Key request = Key.newBuilder()
                .setKey(key)
                .build();
        
        try {
            Value response = blockingStub.get(request);
            String value = response.getValue();
            logger.info("GET successful for key: " + key + ", Value: " + value);
            return value;
        } catch (Exception e) {
            logger.severe("GET failed for key " + key + ": " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Test routing with multiple keys
     */
    public void testRouting() {
        logger.info("Starting routing test...");
        
        // Test keys that should be distributed across partitions
        String[] testKeys = {
            "user:123",
            "user:456", 
            "user:789",
            "product:abc",
            "product:def",
            "product:ghi"
        };
        
        String[] testValues = {
            "John Doe",
            "Jane Smith",
            "Bob Johnson",
            "Laptop",
            "Phone",
            "Tablet"
        };
        
        // PUT operations
        for (int i = 0; i < testKeys.length; i++) {
            put(testKeys[i], testValues[i]);
        }
        
        // GET operations
        for (String key : testKeys) {
            get(key);
        }
        
        logger.info("Routing test completed");
    }
    
    public static void main(String[] args) {
        String host = args.length > 0 ? args[0] : "localhost";
        int port = args.length > 1 ? Integer.parseInt(args[1]) : 9090;
        
        ControlPlaneTestClient client = new ControlPlaneTestClient(host, port);
        
        try {
            // Test basic operations
            client.put("test:key", "test:value");
            String value = client.get("test:key");
            logger.info("Retrieved value: " + value);
            
            // Test routing with multiple keys
            client.testRouting();
            
        } catch (Exception e) {
            logger.severe("Test failed: " + e.getMessage());
        } finally {
            try {
                client.shutdown();
            } catch (InterruptedException e) {
                logger.severe("Failed to shutdown client: " + e.getMessage());
            }
        }
    }
} 