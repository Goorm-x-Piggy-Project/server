FROM openjdk:17-jdk-slim

ADD ./build/libs/account-service-1.0-SNAPSHOT.jar /app/
CMD ["java", "-Xmx200m", "-jar", "/app/account-service-1.0-SNAPSHOT.jar"]

EXPOSE 6000