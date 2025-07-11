FROM gradle:jdk21-alpine AS build
LABEL authors="bravos"

WORKDIR /app
COPY build.gradle settings.gradle ./
COPY gradle ./gradle
COPY src ./src

RUN gradle build -x test --no-daemon

FROM azul/zulu-openjdk-alpine:21-jre-headless-latest AS runtime
WORKDIR /app
COPY .env .env
COPY private.pem ./
COPY public.pem ./
COPY --from=build /app/build/libs/*.jar jwt-auth.jar

ENTRYPOINT ["java", "-Xms1g", "-Xmx1g", "-XX:+UseContainerSupport", "-Duser.timezone=GMT+7", "-jar", "jwt-auth.jar"]