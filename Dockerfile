#
# Build stage
#
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

#
# Package stage
#
FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY --from=build /app/target/app.jar app.jar
EXPOSE ${PORT}
ENTRYPOINT ["java","-jar","app.jar"]
