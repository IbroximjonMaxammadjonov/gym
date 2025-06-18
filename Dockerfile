# 1. Java 17 image'dan foydalanamiz
FROM openjdk:17-jdk-slim

# 2. Jar faylni container ichiga copy qilamiz
COPY target/gym-0.0.1-SNAPSHOT.jar app.jar

# 3. Application'ni ishga tushuramiz
ENTRYPOINT ["java", "-jar", "app.jar"]
