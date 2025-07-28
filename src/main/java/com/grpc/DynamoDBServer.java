package com.grpc;

import com.example.kv.KvStore;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.examples.dynamodb.DynamoDBGrpc;

import java.io.IOException;
import java.util.logging.Logger;

public class DynamoDBServer {
    private static final Logger logger = Logger.getLogger(DynamoDBServer.class.getName());
    private final int port;
    private final Server server;

    public DynamoDBServer(int port, KvStore store) throws IOException {
        this.port = port;
        this.server = ServerBuilder.forPort(port)
                .addService(DynamoDBGrpc.bindService(new DynamoDBService(store)))
                .build();
    }

    /** Starts the server. */
    public void start() throws IOException {
        server.start();
        logger.info("Server started, listening on " + port);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.err.println("*** shutting down gRPC server since JVM is shutting down");
            try {
                DynamoDBServer.this.stop();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.err.println("** server shut down");
        }));
    }

    /** Stops the server. */
    private void stop() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    /** Waits until the server is terminated */
    public void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        KvStore store = new KvStore();
        store.init(); // for loading persisted data

        DynamoDBServer server = new DynamoDBServer(9090, store);
        server.start();
        server.blockUntilShutdown();

    }
}
