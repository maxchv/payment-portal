#### Stage 1: Build the application
FROM maven:3.8.1-jdk-11-slim AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package -DskipTests


#### Stage 2: A docker image with command to run the app
FROM openjdk:11-jre

ARG DEPENDENCY=/home/app/target

COPY --from=build ${DEPENDENCY}/*.jar /usr/local/lib/monitoring.jar

EXPOSE 8093
ENTRYPOINT ["java","-jar","/usr/local/lib/monitoring.jar"]