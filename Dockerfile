FROM openjdk:8-alpine

COPY target/uberjar/sugbi.jar /sugbi/app.jar

EXPOSE 3000

CMD ["java", "-jar", "/sugbi/app.jar"]
