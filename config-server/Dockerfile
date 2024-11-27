FROM eclipse-temurin:17-jdk AS build

WORKDIR /app

COPY ./pom.xml ./mvnw ./
COPY ./.mvn ./.mvn

RUN ./mvnw dependency:go-offline

COPY ./src ./src

RUN ./mvnw clean install -DskipTests

FROM eclipse-temurin:17-jdk-jammy AS runtime

WORKDIR /app

COPY --from=build /app/target/config-server-0.0.1-SNAPSHOT.jar /app/config-server-0.0.1-SNAPSHOT.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/config-server-0.0.1-SNAPSHOT.jar"]