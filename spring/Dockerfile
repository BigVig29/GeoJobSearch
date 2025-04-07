# Stage 1: build
## Start with a base image from Eclipse Temurin (a distribution of OpenJDK) with Alpine Linux, specifying it as the build stage and naming it "build".
FROM openjdk:17-jdk-slim as build
WORKDIR /workspace/app

## Copy necessary files for building the Java project
COPY /geojobsearch .

## Ensure the Gradle Wrapper script is executable
RUN chmod +x ./gradlew

## Use Gradle to build the application using the RUN stanza
RUN ./gradlew build

# Stage 2: runtime
## Start a new stage, using a fresh Alpine-based image from Eclipse Temurin.
FROM openjdk:17-jdk-slim
ARG DEPENDENCY=/workspace/app/build/libs

## Only copy necessary files
COPY --from=build ${DEPENDENCY}/geojobsearch-0.0.1-SNAPSHOT.jar /app/geojobsearch.jar

## Start the Spring application
ENTRYPOINT ["java","-jar","/app/geojobsearch.jar"]
