FROM eclipse-temurin:21-jre

# Working directory inside the container
WORKDIR /app

# Copy the fat jar
COPY build/libs/*-all.jar app.jar

# Cloud Run provides PORT=8080
ENV PORT=8080

# Expose the port
EXPOSE 8080

# Start the server
ENTRYPOINT ["java", "-jar", "app.jar"]
