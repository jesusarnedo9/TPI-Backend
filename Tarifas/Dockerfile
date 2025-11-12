# --- STAGE 1: Build with Maven & Java 17 (Imagen CORREGIDA) ---
# Usamos una imagen de Maven 3.9 con Eclipse Temurin 17 (LTS)
FROM maven:3.9.6-eclipse-temurin-17 AS build

# Set the working directory
WORKDIR /app

# --- Optimización de Capas de Docker ---
# 1. Copia solo los archivos de dependencias
COPY .mvn .mvn
COPY mvnw mvnw
COPY pom.xml .

# 2. Descarga las dependencias (capa de caché)
RUN ./mvnw dependency:go-offline -B

# 3. Copia el código fuente
COPY src src

# 4. Construye la aplicación
RUN ./mvnw -B -Dmaven.test.skip=true package

# --- STAGE 2: Create the final, lightweight runtime image ---
# Usamos la imagen JRE (Runtime) de Java 17
FROM eclipse-temurin:17-jre

# Establece el directorio de trabajo
WORKDIR /app

# Copia el JAR construido desde la etapa 'build'
COPY --from=build /app/target/*.jar app.jar

# El comando para correr la aplicación
# El puerto (8001, 8002, etc.) se define en tu docker-compose.yml
ENTRYPOINT ["java", "-jar", "/app/app.jar"]