# --------------------------
# Stage 1: Build with Maven
# --------------------------
FROM maven:3.9.0-eclipse-temurin-17 AS builder
WORKDIR /workspace

# Copiamos sólo pom y wrapper para cachear deps
COPY pom.xml mvnw ./
COPY .mvn .mvn
RUN chmod +x mvnw

# Descargamos dependencias sin compilar código
RUN ./mvnw dependency:go-offline -B

# Ahora copiamos el código y empaquetamos
COPY src src
RUN ./mvnw clean package -DskipTests -B

# ----------------------------
# Stage 2: Runtime con JRE 17
# ----------------------------
FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app

# Copiamos el JAR compilado desde builder
COPY --from=builder /workspace/target/GAMEDLE-0.0.1-SNAPSHOT.jar app.jar

# Exponemos el puerto que use tu Spring Boot (por defecto 8080)
EXPOSE 8080

# Arrancamos la app
ENTRYPOINT ["java","-jar","/app/app.jar"]