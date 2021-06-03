FROM adoptopenjdk/openjdk11:jre-11.0.10_9
ARG JAR_FILE=./target/*.jar
COPY ${JAR_FILE} application.jar
CMD ["java", "-jar", "application.jar"]
