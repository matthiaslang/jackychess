# docker file to reproduce/test build under new linux systems with latest java versions
FROM maven:3.9.9-eclipse-temurin-21

#ENV DEBIAN_FRONTEND noninteractive
ENV TZ=Europe/Berlin

RUN #apt-get update && apt-get install -y git maven default-jdk

ADD . /

RUN mvn clean package







