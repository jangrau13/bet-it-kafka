FROM openjdk
VOLUME /tmp
ADD target/*.jar app.jar
ADD src/main/resources/application.yaml app/resources/application.yaml
ENV JAVA_OPTS=""
ENTRYPOINT exec java $JAVA_OPTS -jar  /app.jar