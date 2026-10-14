FROM --platform=$BUILDPLATFORM maven:3.9-eclipse-temurin-17 AS build

WORKDIR /build
COPY pom.xml .
RUN mvn --batch-mode --no-transfer-progress dependency:go-offline

COPY src/main ./src/main
RUN mvn --batch-mode --no-transfer-progress package -Dmaven.test.skip=true

FROM amazoncorretto:17 AS runtime

LABEL com.azure.containerizationassist.createdby="containerization-assist"

WORKDIR /app
RUN yum install -y shadow-utils \
    && yum clean all \
    && useradd --create-home --uid 10001 appuser \
    && mkdir -p /app/reports \
    && chown appuser:appuser /app /app/reports

COPY --from=build --chown=appuser:appuser /build/target/semApp.jar /app/app.jar

USER appuser

# This is a batch job; its exit code, not a service health check, indicates completion.
HEALTHCHECK NONE

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
