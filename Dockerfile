FROM openjdk
MAINTAINER Maksym Butusov "maks.butusov@gmail.com"
COPY  target/*.jar ./

EXPOSE 8080
EXPOSE 80

CMD ["java", "-jar", "videoshop-2.0.0.BUILD-SNAPSHOT.jar"]
