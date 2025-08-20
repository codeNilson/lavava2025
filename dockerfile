FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY target/lavava2025-0.9.0.jar app.jar

ENTRYPOINT ["java","-jar","app.jar"]

