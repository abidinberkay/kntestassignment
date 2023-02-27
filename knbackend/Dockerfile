FROM maven:3.9.0-amazoncorretto-17 AS build
COPY src /usr/app/src
COPY pom.xml /usr/app
RUN mvn -f /usr/app/pom.xml clean package

FROM openjdk:17-oracle
EXPOSE 8080
COPY --from=build /usr/app/target/*.jar knbackend.jar
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=DOCKER", "/knbackend.jar"]