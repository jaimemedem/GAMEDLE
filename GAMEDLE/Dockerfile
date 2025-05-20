# --------------------------
# Stage 1: Build con Maven Wrapper + JDK 21
# --------------------------
FROM eclipse-temurin:21-jdk-jammy AS builder
WORKDIR /workspace

# 1) Copiamos pom + wrapper desde la subcarpeta GAMEDLE
COPY GAMEDLE/pom.xml GAMEDLE/mvnw ./
COPY GAMEDLE/.mvn .mvn

# 2) Preparamos el Maven Wrapper offline
RUN chmod +x mvnw \
 && ./mvnw dependency:go-offline -B

# 3) Copiamos el código fuente (toda la carpeta src) desde GAMEDLE/src
COPY GAMEDLE/src src

# 4) Empaquetamos sin tests
RUN ./mvnw clean package -DskipTests -B

# ----------------------------
# Stage 2: Runtime con JRE 21
# ----------------------------
FROM eclipse-temurin:21-jdk-jammy
WORKDIR /app

# 5) Copiamos el JAR resultante
COPY --from=builder /workspace/target/GAMEDLE-0.0.1-SNAPSHOT.jar app.jar

# 6) Exponemos el puerto y arrancamos
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
