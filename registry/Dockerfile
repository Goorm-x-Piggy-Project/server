FROM openjdk:17-jdk-slim

ADD ./build/libs/registry-1.0.0-SNAPSHOT.jar /app/
CMD ["java", "-Xmx200m", "-jar", "/app/registry.jar"]

EXPOSE 8761