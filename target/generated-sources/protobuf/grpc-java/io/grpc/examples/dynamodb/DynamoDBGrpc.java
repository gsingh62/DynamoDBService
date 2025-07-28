package io.grpc.examples.dynamodb;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.63.0)",
    comments = "Source: dynamodb.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class DynamoDBGrpc {

  private DynamoDBGrpc() {}

  public static final java.lang.String SERVICE_NAME = "dynamodb.DynamoDB";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<io.grpc.examples.dynamodb.KeyValue,
      com.google.protobuf.Empty> getPutMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "put",
      requestType = io.grpc.examples.dynamodb.KeyValue.class,
      responseType = com.google.protobuf.Empty.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<io.grpc.examples.dynamodb.KeyValue,
      com.google.protobuf.Empty> getPutMethod() {
    io.grpc.MethodDescriptor<io.grpc.examples.dynamodb.KeyValue, com.google.protobuf.Empty> getPutMethod;
    if ((getPutMethod = DynamoDBGrpc.getPutMethod) == null) {
      synchronized (DynamoDBGrpc.class) {
        if ((getPutMethod = DynamoDBGrpc.getPutMethod) == null) {
          DynamoDBGrpc.getPutMethod = getPutMethod =
              io.grpc.MethodDescriptor.<io.grpc.examples.dynamodb.KeyValue, com.google.protobuf.Empty>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "put"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  io.grpc.examples.dynamodb.KeyValue.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.protobuf.Empty.getDefaultInstance()))
              .setSchemaDescriptor(new DynamoDBMethodDescriptorSupplier("put"))
              .build();
        }
      }
    }
    return getPutMethod;
  }

  private static volatile io.grpc.MethodDescriptor<io.grpc.examples.dynamodb.Key,
      io.grpc.examples.dynamodb.Value> getGetMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "get",
      requestType = io.grpc.examples.dynamodb.Key.class,
      responseType = io.grpc.examples.dynamodb.Value.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<io.grpc.examples.dynamodb.Key,
      io.grpc.examples.dynamodb.Value> getGetMethod() {
    io.grpc.MethodDescriptor<io.grpc.examples.dynamodb.Key, io.grpc.examples.dynamodb.Value> getGetMethod;
    if ((getGetMethod = DynamoDBGrpc.getGetMethod) == null) {
      synchronized (DynamoDBGrpc.class) {
        if ((getGetMethod = DynamoDBGrpc.getGetMethod) == null) {
          DynamoDBGrpc.getGetMethod = getGetMethod =
              io.grpc.MethodDescriptor.<io.grpc.examples.dynamodb.Key, io.grpc.examples.dynamodb.Value>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "get"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  io.grpc.examples.dynamodb.Key.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  io.grpc.examples.dynamodb.Value.getDefaultInstance()))
              .setSchemaDescriptor(new DynamoDBMethodDescriptorSupplier("get"))
              .build();
        }
      }
    }
    return getGetMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static DynamoDBStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<DynamoDBStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<DynamoDBStub>() {
        @java.lang.Override
        public DynamoDBStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new DynamoDBStub(channel, callOptions);
        }
      };
    return DynamoDBStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static DynamoDBBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<DynamoDBBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<DynamoDBBlockingStub>() {
        @java.lang.Override
        public DynamoDBBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new DynamoDBBlockingStub(channel, callOptions);
        }
      };
    return DynamoDBBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static DynamoDBFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<DynamoDBFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<DynamoDBFutureStub>() {
        @java.lang.Override
        public DynamoDBFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new DynamoDBFutureStub(channel, callOptions);
        }
      };
    return DynamoDBFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void put(io.grpc.examples.dynamodb.KeyValue request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getPutMethod(), responseObserver);
    }

    /**
     */
    default void get(io.grpc.examples.dynamodb.Key request,
        io.grpc.stub.StreamObserver<io.grpc.examples.dynamodb.Value> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service DynamoDB.
   */
  public static abstract class DynamoDBImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return DynamoDBGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service DynamoDB.
   */
  public static final class DynamoDBStub
      extends io.grpc.stub.AbstractAsyncStub<DynamoDBStub> {
    private DynamoDBStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected DynamoDBStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new DynamoDBStub(channel, callOptions);
    }

    /**
     */
    public void put(io.grpc.examples.dynamodb.KeyValue request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getPutMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void get(io.grpc.examples.dynamodb.Key request,
        io.grpc.stub.StreamObserver<io.grpc.examples.dynamodb.Value> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service DynamoDB.
   */
  public static final class DynamoDBBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<DynamoDBBlockingStub> {
    private DynamoDBBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected DynamoDBBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new DynamoDBBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.google.protobuf.Empty put(io.grpc.examples.dynamodb.KeyValue request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getPutMethod(), getCallOptions(), request);
    }

    /**
     */
    public io.grpc.examples.dynamodb.Value get(io.grpc.examples.dynamodb.Key request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service DynamoDB.
   */
  public static final class DynamoDBFutureStub
      extends io.grpc.stub.AbstractFutureStub<DynamoDBFutureStub> {
    private DynamoDBFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected DynamoDBFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new DynamoDBFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.google.protobuf.Empty> put(
        io.grpc.examples.dynamodb.KeyValue request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getPutMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<io.grpc.examples.dynamodb.Value> get(
        io.grpc.examples.dynamodb.Key request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_PUT = 0;
  private static final int METHODID_GET = 1;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final AsyncService serviceImpl;
    private final int methodId;

    MethodHandlers(AsyncService serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_PUT:
          serviceImpl.put((io.grpc.examples.dynamodb.KeyValue) request,
              (io.grpc.stub.StreamObserver<com.google.protobuf.Empty>) responseObserver);
          break;
        case METHODID_GET:
          serviceImpl.get((io.grpc.examples.dynamodb.Key) request,
              (io.grpc.stub.StreamObserver<io.grpc.examples.dynamodb.Value>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          getPutMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              io.grpc.examples.dynamodb.KeyValue,
              com.google.protobuf.Empty>(
                service, METHODID_PUT)))
        .addMethod(
          getGetMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              io.grpc.examples.dynamodb.Key,
              io.grpc.examples.dynamodb.Value>(
                service, METHODID_GET)))
        .build();
  }

  private static abstract class DynamoDBBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    DynamoDBBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return io.grpc.examples.dynamodb.DynamoDBProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("DynamoDB");
    }
  }

  private static final class DynamoDBFileDescriptorSupplier
      extends DynamoDBBaseDescriptorSupplier {
    DynamoDBFileDescriptorSupplier() {}
  }

  private static final class DynamoDBMethodDescriptorSupplier
      extends DynamoDBBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    DynamoDBMethodDescriptorSupplier(java.lang.String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (DynamoDBGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new DynamoDBFileDescriptorSupplier())
              .addMethod(getPutMethod())
              .addMethod(getGetMethod())
              .build();
        }
      }
    }
    return result;
  }
}
