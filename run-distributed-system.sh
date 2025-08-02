#!/bin/bash

# Distributed Key-Value Store Runner
# This script starts the control plane and partition servers

echo "Building the project..."
mvn clean package

echo ""
echo "Starting partition servers..."

# Start partition servers in background
echo "Starting partition-0 on port 9091..."
java -cp "target/classes:target/dependency/*" com.grpc.PartitionServer 9091 &
PARTITION_0_PID=$!

echo "Starting partition-1 on port 9092..."
java -cp "target/classes:target/dependency/*" com.grpc.PartitionServer 9092 &
PARTITION_1_PID=$!

echo "Starting partition-2 on port 9093..."
java -cp "target/classes:target/dependency/*" com.grpc.PartitionServer 9093 &
PARTITION_2_PID=$!

# Wait a moment for partition servers to start
sleep 5

echo ""
echo "Starting control plane server..."
echo "Control plane will route requests to partitions on ports 9091, 9092, 9093"

# Start control plane server
java -cp "target/classes:target/dependency/*" com.grpc.ControlPlaneServer 9090 3 9091

# Cleanup function
cleanup() {
    echo ""
    echo "Shutting down all servers..."
    kill $PARTITION_0_PID 2>/dev/null
    kill $PARTITION_1_PID 2>/dev/null
    kill $PARTITION_2_PID 2>/dev/null
    exit 0
}

# Set up signal handlers
trap cleanup SIGINT SIGTERM

# Wait for control plane to finish
wait 