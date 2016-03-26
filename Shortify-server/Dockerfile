FROM java:8
MAINTAINER Giuseppe Perniola <g.perniola22@gmail.com>

EXPOSE 4567

#install maven
RUN apt-get update
RUN apt-get install -y maven

WORKDIR /code

COPY . /code

# Prepare by downloading dependencies
RUN ["mvn", "dependency:resolve"]
RUN ["mvn", "verify"]


# compile and package into a fat jar
RUN ["mvn","package"]

CMD ["/usr/lib/jvm/java-8-openjdk-amd64/bin/java","-jar","target/shortify-server-jar-with-dependencies.jar"]
