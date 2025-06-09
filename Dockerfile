FROM openjdk:21-jdk-slim
WORKDIR /app
COPY build/libs/connecple_backend-0.0.1-SNAPSHOT.jar app.jar
ENV TZ=Asia/Seoul
ENTRYPOINT ["java", "-jar", "app.jar"]