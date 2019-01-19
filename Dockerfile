FROM maven:3.6-jdk-8-alpine

COPY ./code /application
WORKDIR /application
RUN mvn clean install

EXPOSE 8080

ENTRYPOINT ["mvn", "spring-boot:run"]
#CMD ["java", "Main"]