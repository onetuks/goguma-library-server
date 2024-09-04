FROM openjdk:21-jdk
ARG JAR_FILE=*.jar
COPY ${JAR_FILE} goguma-library-server.jar
CMD ["java", "-jar", "goguma-library-server.jar"]