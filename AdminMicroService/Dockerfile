# Use Amazon Corretto 8 for the runtime
FROM openjdk:8

# Copy the manually built JAR file from your local target directory into the container
COPY target/AdminMicroService-0.0.1-SNAPSHOT.jar admin.jar

# Expose port 8081 for the Spring Boot application
EXPOSE 8081

# Set the default command to run the JAR
ENTRYPOINT ["java", "-jar", "/admin.jar"]

