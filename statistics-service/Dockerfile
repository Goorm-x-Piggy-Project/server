FROM openjdk:17-jdk-slim
MAINTAINER Alexander Lukyanchikov <sqshq@sqshq.com>

ADD ./build/libs/statistics-service.jar /app/
CMD ["java", "-Xmx200m", "-jar", "/app/statistics-service.jar"]

EXPOSE 7000