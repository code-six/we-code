# Stage 1: Build the application
FROM krmp-d2hub-idock.9rum.cc/goorm/gradle:8.2.1-jdk17 as build

WORKDIR /home/gradle/project

COPY . .

RUN echo "systemProp.http.proxyHost=krmp-proxy.9rum.cc\nsystemProp.http.proxyPort=3128\nsystemProp.https.proxyHost=krmp-proxy.9rum.cc\nsystemProp.https.proxyPort=3128" > /root/.gradle/gradle.properties

RUN gradle wrapper

RUN ./gradlew clean build

# Stage 2: Run the application
FROM krmp-d2hub-idock.9rum.cc/goorm/eclipse-temurin:17-jre

COPY --from=build /home/gradle/project/build/libs/wecode-0.0.1.jar .

#ENV DATABASE_URL=jdbc:mariadb://mariadb/kakao

CMD ["java", "-jar", "-Dspring.profiles.active=prod", "wecode-0.0.1.jar"]