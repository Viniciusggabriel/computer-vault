FROM maven:3.9.6-amazoncorretto-21 AS builder
LABEL authors="VINICIUS-GABRIEL"
LABEL name="spring-computer-vault-api"

COPY src /app/src
COPY pom.xml /app

WORKDIR /app
RUN mvn clean package

# .env file
ARG POSTGRES_DB
ARG POSTGRES_PASSWORD
ARG POSTGRES_USER
ARG POSTGRES_DB_URL

RUN echo "POSTGRES_DB=$POSTGRES_DB" >> .env
RUN echo "POSTGRES_PASSWORD=$POSTGRES_PASSWORD" >> .env
RUN echo "POSTGRES_USER=$POSTGRES_USER" >> .env
RUN echo "POSTGRES_DB_URL=$POSTGRES_DB_URL" >> .env

FROM amazoncorretto:21 AS runner

COPY --from=builder /app/target/server-0.0.1-SNAPSHOT.jar /app/app.jar

WORKDIR /app

EXPOSE 8008

CMD ["java", "-jar", "app.jar"]