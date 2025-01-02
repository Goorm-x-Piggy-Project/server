
FROM openjdk:17-jdk-slim
MAINTAINER Alexander Lukyanchikov <sqshq@sqshq.com>

ADD ./build/libs/notification-service.jar /app/
CMD ["java", "-Xmx200m", "-jar", "/app/notification-service.jar"]

EXPOSE 8000
