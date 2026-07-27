# ---------- Build Stage ----------
FROM gradle:8.10.2-jdk21 AS builder

WORKDIR /app

# Copy the entire project
        COPY . .

# Give execution permission to Gradle wrapper
        RUN chmod +x gradlew

# Build the fat JAR
        RUN ./gradlew buildFatJar --no-daemon

# ---------- Runtime Stage ----------
FROM eclipse-temurin:21-jre

WORKDIR /app

# Copy the generated fat JAR
COPY --from=builder /app/build/libs/*-all.jar app.jar

# Render provides PORT automatically
ENV PORT=8080

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]