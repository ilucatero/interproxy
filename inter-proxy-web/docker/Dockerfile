# Dockerfile

#FROM  phusion/baseimage:0.9.17
FROM  ilucatero/interproxy:latest

MAINTAINER  Ignacio Lucatero <user@email.com>

# Make port 80 available to the world outside this container
EXPOSE 8080

ARG JAR_FILE
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]



