FROM eclipse-temurin:21-jre

WORKDIR /app

COPY target/ms-historial-service-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8088

ENTRYPOINT ["java", "-jar", "app.jar"]
