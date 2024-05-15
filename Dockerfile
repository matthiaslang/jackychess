# docker file to reproduce/test build under new linux systems with latest java versions
FROM ubuntu:24.04

ENV DEBIAN_FRONTEND noninteractive
ENV TZ=Europe/Berlin

RUN apt-get update && apt-get install -y git maven default-jdk

ADD . /

RUN mvn clean package







