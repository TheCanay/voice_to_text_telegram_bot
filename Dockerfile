FROM openjdk:21-jdk-slim
LABEL author=TheCanay

WORKDIR /app

COPY build/libs/*.jar target/app.jar

CMD ["java", "-jar", "target/app.jar"]