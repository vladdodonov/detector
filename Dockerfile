FROM adoptopenjdk/openjdk11:alpine as build

WORKDIR /app
COPY ./.mvn ./.mvn
COPY ./mvnw .
COPY ./pom.xml .
RUN chmod a+x ./mvnw
RUN ./mvnw -B -e -C -T 1C dependency:go-offline

COPY . .
RUN chmod a+x ./mvnw

RUN ./mvnw -B -e clean package -Dmaven.test.skip=t

FROM adoptopenjdk/openjdk11:alpine
COPY --from=build /app/target/*.jar /detector-1.0.jar
ENTRYPOINT ["java", "-Xmx4096m", "-Xms256m", "-jar", "app.jar"]

