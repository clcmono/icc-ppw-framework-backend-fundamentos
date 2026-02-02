# =========================
# Stage 1: Build
# =========================
FROM gradle:8.14-jdk17 AS build
WORKDIR /app

# Copiar archivos de configuración para cachear dependencias
COPY build.gradle.kts settings.gradle.kts ./
COPY gradle ./gradle

RUN gradle dependencies --no-daemon

# Copiar el código fuente
COPY src ./src

# Construir el JAR ejecutable
RUN gradle bootJar --no-daemon

# =========================
# Stage 2: Runtime
# =========================
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copiar el JAR generado
COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]
