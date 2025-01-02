FROM openjdk:17-jdk-slim

ADD ./build/libs/auth-service-1.0-SNAPSHOT.jar /app/
CMD ["java", "-Xmx200m", "-jar", "/app/auth-service-1.0-SNAPSHOT.jar"]

#HEALTHCHECK --interval=30s --timeout=30s CMD curl -f http://localhost:5000/.well-known/openid-configuration || exit 1

EXPOSE 5000