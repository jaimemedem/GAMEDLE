# --------------------------
# Stage 1: Build con Maven Wrapper + JDK 21
# --------------------------
FROM eclipse-temurin:21-jdk-jammy AS builder
WORKDIR /workspace

# Copiamos el POM, el wrapper y la carpeta .mvn
COPY pom.xml mvnw ./
COPY .mvn .mvn

# Preparamos el wrapper + dependencias offline
RUN chmod +x mvnw \
 && ./mvnw dependency:go-offline -B

# Copiamos todo el código fuente y empaquetamos sin tests
COPY src src
RUN ./mvnw clean package -DskipTests -B

# ----------------------------
# Stage 2: Runtime con JRE 21
# ----------------------------
FROM eclipse-temurin:21-jdk-jammy
WORKDIR /app

# Copiamos el JAR ya construido
COPY --from=builder /workspace/target/GAMEDLE-0.0.1-SNAPSHOT.jar app.jar

# Puerto que usa Spring Boot por defecto
EXPOSE 8080

# Arrancamos la aplicación
ENTRYPOINT ["java","-jar","/app/app.jar"]
