# Use Amazon Corretto 8 for the runtime
FROM openjdk:8

# Copy the manually built JAR file from your local target directory into the container
COPY target/BooksMicroService-0.0.1-SNAPSHOT.jar book.jar

# Expose port 8082 for the Spring Boot application
EXPOSE 8082

# Set the default command to run the JAR
ENTRYPOINT ["java", "-jar", "/book.jar"]

