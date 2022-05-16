FROM openjdk:8
EXPOSE 8080
ADD target/apps.jar apps.jar
ENTRYPOINT [ "java", "-jar", "/apps.jar" ]
