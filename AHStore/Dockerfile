##Dockerfile is where we define the docker image and specify all the configurations required to run the app.

#Dockerfile for creating image of Alpine + mongodb + java jdk env for Fullstack2.

FROM openjdk:8u171-jdk-alpine3.8

RUN apk add --no-cache mongodb

VOLUME /data/db
#EXPOSE 27017 28017

ADD createMongoConfig.js /tmp/

# Expose MongoDB ports
EXPOSE 27017 28017

#CMD [ "mongod", "--bind_ip", "192.168.99.101" ]
CMD [ "mongo", "use tekhealth" ]


# The application's jar file
ARG JAR_FILE=target/TEKHealthAPI-0.0.1-SNAPSHOT.jar

# Add the application's jar to the container
ADD ${JAR_FILE} TEKHealthAPI.jar

# Run the jar file
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/TEKHealthAPI.jar"]

EXPOSE 8081 4111