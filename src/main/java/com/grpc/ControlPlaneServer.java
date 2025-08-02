package com.grpc;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.examples.dynamodb.DynamoDBGrpc;

import java.io.IOException;
import java.util.logging.Logger;

public class ControlPlaneServer {
    private static final Logger logger = Logger.getLogger(ControlPlaneServer.class.getName());
    
    private final int port;
    private final Server server;
    private final ControlPlaneService controlPlaneService;
    
    public ControlPlaneServer(int port, int numPartitions, String[] partitionHosts, int partitionPort) throws IOException {
        this.port = port;
        this.controlPlaneService = new ControlPlaneService(numPartitions, partitionHosts, partitionPort);
        
        this.server = ServerBuilder.forPort(port)
                .addService(DynamoDBGrpc.bindService(controlPlaneService))
                .build();
    }
    
    /** Starts the server. */
    public void start() throws IOException {
        server.start();
        logger.info("Control Plane Server started, listening on " + port);
        
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("*** shutting down Control Plane gRPC server since JVM is shutting down");
            try {
                ControlPlaneServer.this.stop();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            logger.info("*** server shut down");
        }));
    }
    
    /** Stops the server. */
    private void stop() throws InterruptedException {
        if (server != null) {
            server.shutdown();
        }
        if (controlPlaneService != null) {
            controlPlaneService.shutdown();
        }
    }
    
    /** Waits until the server is terminated */
    public void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }
    
    public ControlPlaneService getControlPlaneService() {
        return controlPlaneService;
    }
    
    public static void main(String[] args) throws IOException, InterruptedException {
        int port = args.length > 0 ? Integer.parseInt(args[0]) : 9090;
        int numPartitions = args.length > 1 ? Integer.parseInt(args[1]) : 3;
        int partitionPort = args.length > 2 ? Integer.parseInt(args[2]) : 9091;
        
        // Parse partition hosts if provided
        String[] partitionHosts = null;
        if (args.length > 3) {
            partitionHosts = args[3].split(",");
        }
        
        logger.info("Starting Control Plane Server with " + numPartitions + " partitions on port " + port);
        logger.info("Partition processes expected on ports " + partitionPort + " to " + (partitionPort + numPartitions - 1));
        
        ControlPlaneServer server = new ControlPlaneServer(port, numPartitions, partitionHosts, partitionPort);
        server.start();
        server.blockUntilShutdown();
    }
} 