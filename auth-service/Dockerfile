FROM openjdk:17-jdk-slim

ADD ./build/libs/account-service-1.0-SNAPSHOT.jar /app/
CMD ["java", "-Xmx200m", "-jar", "/app/auth-service.jar"]

EXPOSE 5000