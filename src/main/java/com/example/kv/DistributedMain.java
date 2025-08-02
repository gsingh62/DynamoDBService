package com.example.kv;

import com.grpc.ControlPlaneServer;
import com.grpc.PartitionServer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import java.io.IOException;
import java.util.logging.Logger;

@SpringBootApplication
public class DistributedMain {
    private static final Logger logger = Logger.getLogger(DistributedMain.class.getName());
    
    public static void main(String[] args) {
        // Check if we should run in distributed mode
        if (args.length > 0 && (args[0].startsWith("--partition.server.port") || 
                                args[0].startsWith("--control.plane.port"))) {
            runDistributedMode(args);
        } else {
            // Run normal Spring Boot application
            SpringApplication.run(DistributedMain.class, args);
        }
    }
    
    private static void runDistributedMode(String[] args) {
        try {
            // Parse arguments
            int partitionPort = -1;
            int controlPlanePort = -1;
            int numPartitions = 3;
            int partitionBasePort = 9091;
            
            for (String arg : args) {
                if (arg.startsWith("--partition.server.port=")) {
                    partitionPort = Integer.parseInt(arg.split("=")[1]);
                } else if (arg.startsWith("--control.plane.port=")) {
                    controlPlanePort = Integer.parseInt(arg.split("=")[1]);
                } else if (arg.startsWith("--partitions=")) {
                    numPartitions = Integer.parseInt(arg.split("=")[1]);
                } else if (arg.startsWith("--partition.base.port=")) {
                    partitionBasePort = Integer.parseInt(arg.split("=")[1]);
                }
            }
            
            if (partitionPort > 0) {
                // Start partition server
                logger.info("Starting Partition Server on port " + partitionPort);
                PartitionServer server = new PartitionServer(partitionPort);
                server.start();
                server.blockUntilShutdown();
            } else if (controlPlanePort > 0) {
                // Start control plane server
                logger.info("Starting Control Plane Server on port " + controlPlanePort);
                logger.info("Partition processes expected on ports " + partitionBasePort + " to " + (partitionBasePort + numPartitions - 1));
                ControlPlaneServer server = new ControlPlaneServer(controlPlanePort, numPartitions, null, partitionBasePort);
                server.start();
                server.blockUntilShutdown();
            } else {
                logger.severe("Invalid arguments. Use --partition.server.port=X or --control.plane.port=X");
                System.exit(1);
            }
            
        } catch (Exception e) {
            logger.severe("Failed to start distributed server: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
} 