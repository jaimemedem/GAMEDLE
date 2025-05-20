# --------------------------
# Stage 1: Build with Maven
# --------------------------
FROM maven:3.9.0-eclipse-temurin-21 AS builder
WORKDIR /workspace

# Copiamos el pom y el wrapper desde la carpeta GAMEDLE
COPY GAMEDLE/pom.xml GAMEDLE/mvnw ./
COPY GAMEDLE/.mvn .mvn
RUN chmod +x mvnw \
 && ./mvnw dependency:go-offline -B

# Copiamos el código fuente (que está en GAMEDLE/src) al directorio src
COPY GAMEDLE/src src
RUN ./mvnw clean package -DskipTests -B

# ----------------------------
# Stage 2: Runtime con JRE 17
# ----------------------------
FROM eclipse-temurin:21-jdk-jammy
WORKDIR /app

# El JAR resultante está en /workspace/target dentro del builder
COPY --from=builder /workspace/target/GAMEDLE-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080

ENTRYPOINT ["java","-jar","/app/app.jar"]
