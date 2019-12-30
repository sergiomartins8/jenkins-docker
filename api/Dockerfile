FROM maven:3-jdk-8 as BUILD

COPY . /usr/src/app
RUN mvn --batch-mode -f /usr/src/app/pom.xml clean package

FROM openjdk:8-jre-slim
COPY --from=BUILD /usr/src/app/target /opt/target
WORKDIR /opt/target

ENTRYPOINT ["java", "-jar", "restful.phonebook-0.1.jar"]