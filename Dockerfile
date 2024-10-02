From openjdk:11
ADD ./build/libs/dvd-0.0.1-SNAPSHOT.jar dvd-0.0.1-SNAPSHOT.jar
EXPOSE 3000
ENTRYPOINT ["java", "-jar", "dvd-0.0.1-SNAPSHOT.jar"]