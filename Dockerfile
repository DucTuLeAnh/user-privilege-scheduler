FROM docker.io/library/openjdk:8-jre-alpine
COPY build/libs/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]





