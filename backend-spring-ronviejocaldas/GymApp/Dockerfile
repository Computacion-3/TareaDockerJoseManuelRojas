FROM maven:3.9-eclipse-temurin-17

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn -B -DskipTests package

EXPOSE 8080

CMD ["java", "-jar", "target/icesi-fit-0.0.1-SNAPSHOT.war"]
