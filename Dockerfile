FROM gradle:6.4.1-jdk14 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build

FROM openjdk:14-jdk-alpine
RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/airwaves-web.jar /app/app.jar
ENTRYPOINT ["java","-jar","/app/app.jar"]