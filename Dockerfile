# Use an official OpenJDK runtime as a parent image
FROM openjdk:23-rc-jdk-slim

# Install curl and upgrade existing packages
RUN apt-get update && apt-get install -y curl && \
    apt-get upgrade -y && \
    rm -rf /var/lib/apt/lists/* \

# Set the working directory inside the container
WORKDIR /app

# Copy the application's jar file to the container
ARG JAR_FILE=target/payment-demo-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar

# Expose the port that the application will run on
EXPOSE 8080

# Run the jar file
ENTRYPOINT ["java","-jar","app.jar"]