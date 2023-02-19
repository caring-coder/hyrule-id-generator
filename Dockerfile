FROM openjdk:19-jdk
ARG JAR_FILE=target/*-jar-with-dependencies.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]