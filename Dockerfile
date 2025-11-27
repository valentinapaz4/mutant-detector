# Etapa 1: Build
FROM gradle:8.5-jdk21 AS build

WORKDIR /app

# Copiar archivos de Gradle
COPY build.gradle settings.gradle gradlew ./
COPY gradle ./gradle

# Copiar código fuente
COPY src ./src

# Construir el JAR (sin tests)
RUN ./gradlew clean bootJar -x test --no-daemon

# Etapa 2: Runtime
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copiar solo el JAR desde la etapa de build
COPY --from=build /app/build/libs/*.jar app.jar

# Exponer el puerto
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-Dserver.port=${PORT:-8080}", "-Xmx512m", "-jar", "app.jar"]