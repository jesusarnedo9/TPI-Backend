# --- STAGE 1: Build with Maven & Java 17 ---
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# --- Optimización de Capas de Docker ---
# 1. Copia solo el pom.xml
COPY pom.xml .

# 2. Descarga las dependencias (capa de caché)
# CORRECCIÓN: Usamos 'mvn' (que está en la imagen) en lugar de './mvnw' (que no existe)
RUN mvn dependency:go-offline -B

# 3. Copia el código fuente
COPY src src

# 4. Construye la aplicación
# CORRECCIÓN: Usamos 'mvn' en lugar de './mvnw'
RUN mvn package -Dmaven.test.skip=true -B

# --- STAGE 2: Create the final, lightweight runtime image ---
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copia el JAR construido desde la etapa 'build'
# Usamos un comodín (*) para que coincida con cualquier nombre de .jar
COPY --from=build /app/target/*.jar app.jar

# El comando para correr la aplicación
ENTRYPOINT ["java", "-jar", "/app/app.jar"]

