FROM openjdk:17-alpine
VOLUME /tmp
RUN apk add --no-cache libstdc++
ADD target/*.jar app.jar
ADD src/main/resources/application.yml app/resources/application.properties
ENV JAVA_OPTS=""
ENTRYPOINT exec java $JAVA_OPTS -jar  /app.jar