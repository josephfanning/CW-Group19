FROM openjdk:latest
COPY ./target/Group19-0.1-alpha-3-jar-with-dependencies.jar /tmp
WORKDIR /tmp
ENTRYPOINT ["java", "-jar", "Group19-0.1-alpha-3-jar-with-dependencies.jar"]
