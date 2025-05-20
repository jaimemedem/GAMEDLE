# --------------------------
# Stage 1: Build with Maven Wrapper on JDK 21
# --------------------------
FROM eclipse-temurin:21-jdk-jammy AS builder
WORKDIR /workspace

# Copiamos pom + maven‐wrapper
COPY GAMEDLE/pom.xml GAMEDLE/mvnw ./
COPY GAMEDLE/.mvn .mvn

RUN chmod +x mvnw \
 && ./mvnw dependency:go-offline -B

# Copiamos el código y compilamos
COPY GAMEDLE/src src
RUN ./mvnw clean package -DskipTests -B

# ----------------------------
# Stage 2: Runtime con JRE 21
# ----------------------------
FROM eclipse-temurin:21-jdk-jammy
WORKDIR /app

COPY --from=builder /workspace/target/GAMEDLE-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080

ENTRYPOINT ["java","-jar","/app/app.jar"]
