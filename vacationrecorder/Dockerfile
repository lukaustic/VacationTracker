# Stage 1: Build the project using Maven
FROM maven:latest AS build
COPY . /usr/src/app
WORKDIR /usr/src/app
RUN mvn clean package

# Stage 2: Run the application using the JAR file
FROM eclipse-temurin:17
WORKDIR /app
COPY --from=build /usr/src/app/target/*.jar /app/vacationimport.jar
ENTRYPOINT ["java","-jar","/app/vacationimport.jar"]