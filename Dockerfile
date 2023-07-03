#FROM eclipse-temurin:17-jdk-jammy as base
#WORKDIR /app
#COPY .mvn/ .mvn
#COPY mvnw pom.xml ./
#RUN ./mvnw dependency:resolve
#COPY src ./src

#FROM base as development
#CMD ["./mvnw", "spring-boot:run", "-Dspring-boot.run.profiles=mysql", "-Dspring-boot.run.jvmArguments='-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:8000'"]

#FROM base as build
#RUN ./mvnw package

#FROM eclipse-temurin:17-jre-jammy as production
#EXPOSE 8080
#COPY  /build/libs/*.jar application.jar
#CMD ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "application.jar"]

FROM maven:3.8.4-openjdk-17 AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package -Dmaven.test.skip=true


#
# Package stage
#
FROM openjdk:17-oracle
COPY --from=build /home/app/target/doc-recognizer-0.0.1-SNAPSHOT.jar /usr/local/lib/doc-recognizer.jar
ENTRYPOINT ["java","-jar","/usr/local/lib/doc-recognizer.jar"]