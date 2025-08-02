#!/bin/bash

# Test Client Runner
# This script runs the test client against the distributed system

echo "Running test client..."

# Run the test client
java -cp "target/classes:target/dependency/*" com.grpc.ControlPlaneTestClient

echo "Test client completed." 