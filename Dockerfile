FROM adoptopenjdk/openjdk11:alpine-slim
# cd /opt/app
WORKDIR /opt/app


COPY target/*.jar app.jar 
COPY target/dependency/*-*.jar ./ 
COPY target/dependency/aspectjweaver.jar . 
COPY newrelic.yml . 

ENV JAVA_OPTS=
# java -jar /opt/app/app.jar
ENTRYPOINT java ${JAVA_OPTS} -javaagent:newrelic-agent.jar "-Dnewrelic.environment=${SPRING_PROFILES_ACTIVE}" -jar app.jar
