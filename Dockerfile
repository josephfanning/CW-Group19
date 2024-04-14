# specifies the image the dockerfile will be using
FROM openjdk:latest
# copies the jar file from the target directory into the /tmp file
COPY ./target/Group19.jar /tmp
# sets the working directory in the container
WORKDIR /tmp
# starts up the VM using the -jar option (specifies what jar file to execute)
ENTRYPOINT ["java", "-jar", "Group19.jar", "db:3306", "10000"]