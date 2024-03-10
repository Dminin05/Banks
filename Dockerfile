FROM openjdk:21-jdk
COPY build/libs/Banks-0.0.1-SNAPSHOT.jar banks.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "banks.jar"]