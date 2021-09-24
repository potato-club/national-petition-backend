FROM adoptopenjdk/openjdk11

COPY . app/

WORKDIR app

RUN ./gradlew clean bootJar

ENTRYPOINT ["java", "-jar", "build/libs/national-petition-backend.jar"]