# ---------------------------------------------------------------------
# (1) build stage
# ---------------------------------------------------------------------
FROM maven:3-eclipse-temurin-21-alpine AS builder

ADD . /build
WORKDIR /build

# Build application
RUN mvn --no-transfer-progress clean package -DskipTests=true

# ---------------------------------------------------------------------
# (2) run stage
# ---------------------------------------------------------------------
FROM eclipse-temurin:21-jre-alpine

COPY --from=builder "/build/target/cluster-namer-*.jar"  /srv/cluster-namer.jar

RUN apk add curl

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/srv/cluster-namer.jar"]
