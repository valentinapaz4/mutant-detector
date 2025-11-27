# Etapa 1: Build
FROM gradle:8.5-jdk21 AS build

WORKDIR /app

# Copiar archivos de configuración de Gradle
COPY build.gradle settings.gradle ./
COPY gradle ./gradle
COPY gradlew ./

# Dar permisos de ejecución a gradlew
RUN chmod +x gradlew

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

# Variables de entorno por defecto
ENV PORT=8080
ENV JAVA_OPTS="-Xmx512m"

# Comando para ejecutar la aplicación
ENTRYPOINT java $JAVA_OPTS -Dserver.port=$PORT -jar app.jar