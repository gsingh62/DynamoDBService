# Use an official JDK base image
FROM openjdk:17-jdk-slim

# Copy built jar
COPY target/kv-store-0.0.1-SNAPSHOT.jar app.jar

# Run app
ENTRYPOINT ["java", "-jar", "app.jar"]

