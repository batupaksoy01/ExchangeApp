FROM openjdk:11
EXPOSE 8080
ADD target/exchange-application.jar exchange-application.jar
ENTRYPOINT ["java","-jar","/exchange-application.jar"]