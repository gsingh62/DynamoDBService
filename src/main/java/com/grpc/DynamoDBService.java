package com.grpc;

import com.example.kv.KvStore;
import com.google.protobuf.Empty;
import io.grpc.examples.dynamodb.DynamoDBGrpc;
import io.grpc.examples.dynamodb.Value;

public class DynamoDBService implements DynamoDBGrpc.AsyncService {
    private final KvStore store;

    public DynamoDBService(KvStore store) {
        this.store = store;
    }

    @Override
    public void put(io.grpc.examples.dynamodb.KeyValue request,
                    io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
        store.put(request.getKey(), request.getValue(), false);
        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }

    @Override
    public void get(io.grpc.examples.dynamodb.Key request,
                    io.grpc.stub.StreamObserver<io.grpc.examples.dynamodb.Value> responseObserver) {

        String val = store.get(request.getKey());
        Value response = Value.newBuilder()
                .setValue(val != null ? val : "")
                        .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
