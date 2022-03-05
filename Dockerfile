FROM openjdk:8
EXPOSE 5000
ADD target/apps.jar apps.jar
ENTRYPOINT [ "java", "-jar", "/apps.jar" ]