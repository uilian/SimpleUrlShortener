FROM openjdk:8-alpine

COPY ./code/target/URLShortener*.jar /application/URLShortener.jar
WORKDIR /application

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "URLShortener.jar"]
#CMD ["java", "Main"]