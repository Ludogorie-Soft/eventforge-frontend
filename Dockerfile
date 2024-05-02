FROM openjdk:17
VOLUME /tmp
EXPOSE 8180
ARG JAR_FILE=target/EventForge-Frontend-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]