# Dockerfile

#FROM  phusion/baseimage:0.9.17
FROM  ubuntu:18.04

MAINTAINER  Ignacio Lucatero <user@email.com>

RUN apt-get -y update

RUN apt-get install sudo

# Install python-software-properties
RUN sudo apt --fix-broken install -y python-pycurl python-apt
RUN DEBIAN_FRONTEND=noninteractive apt-get install -y -q software-properties-common

# Install Oracle Java
ENV JAVA_VER 10
ENV JAVA_HOME /usr/lib/jvm/java-${JAVA_VER}-oracle

RUN sudo add-apt-repository ppa:linuxuprising/java && \
    sudo apt update

RUN echo oracle-java${JAVA_VER}-installer shared/accepted-oracle-license-v1-1 select true | sudo /usr/bin/debconf-set-selections && \
    sudo apt install -y oracle-java${JAVA_VER}-installer && \
    sudo apt remove oracle-java${JAVA_VER}-set-default ;  sudo apt install oracle-java${JAVA_VER}-set-default


RUN apt-get clean && \
    sudo rm -rf /var/cache/oracle-jdk${JAVA_VER}-installer

# Set Oracle Java as the default Java
RUN update-java-alternatives -s java-${JAVA_VER}-oracle

RUN echo "export JAVA_HOME=/usr/lib/jvm/java-${JAVA_VER}-oracle" >> ~/.bashrc

# Clean Up APT when finished
RUN apt-get clean && rm -rf /var/lib/apt/lists/* /tmp/* /var/tmp/*
