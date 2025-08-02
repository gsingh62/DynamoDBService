package com.grpc;

import com.example.kv.KvStore;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.examples.dynamodb.DynamoDBGrpc;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * Partition Server - runs as a separate process to handle requests for a specific partition
 */
public class PartitionServer {
    private static final Logger logger = Logger.getLogger(PartitionServer.class.getName());
    
    private final int port;
    private final Server server;
    private final DynamoDBService dynamoDBService;
    private final KvStore store;
    
    public PartitionServer(int port) throws IOException {
        this.port = port;
        this.store = new KvStore();
        this.dynamoDBService = new DynamoDBService(store);
        
        this.server = ServerBuilder.forPort(port)
                .addService(DynamoDBGrpc.bindService(dynamoDBService))
                .build();
    }
    
    /** Starts the server. */
    public void start() throws IOException {
        server.start();
        logger.info("Partition Server started, listening on " + port);
        
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("*** shutting down Partition Server since JVM is shutting down");
            try {
                PartitionServer.this.stop();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            logger.info("*** partition server shut down");
        }));
    }
    
    /** Stops the server. */
    private void stop() throws InterruptedException {
        if (server != null) {
            server.shutdown();
        }
    }
    
    /** Waits until the server is terminated */
    public void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }
    
    public static void main(String[] args) throws IOException, InterruptedException {
        int port = args.length > 0 ? Integer.parseInt(args[0]) : 9091;
        
        logger.info("Starting Partition Server on port " + port);
        
        PartitionServer server = new PartitionServer(port);
        server.start();
        server.blockUntilShutdown();
    }
} 