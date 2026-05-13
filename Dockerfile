# Usamos una imagen ligera de Java
FROM eclipse-temurin:17-jdk-alpine
LABEL maintainer="alesia rubtsova"

# Creamos un volumen temporal (Spring Boot lo usa por debajo)
VOLUME /tmp

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java","-jar","/app.jar"]