FROM openjdk:11

ENV PROJECT_NAME "db-controller-api"
ENV WORKSPACE_PATH "/workspace"
ENV PROJECT_PATH $WORKSPACE_PATH/$PROJECT_NAME
ENV SCRIPTS_PATH $PROJECT_PATH/Dockerfiles/scripts

WORKDIR $PROJECT_PATH

COPY . .

RUN chmod -R u+rx $SCRIPTS_PATH/*

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

ENTRYPOINT ["/bin/bash","Dockerfiles/scripts/entrypoint.sh"]
