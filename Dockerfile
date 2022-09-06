## yacy_grid_search dockerfile
## examples:
# docker build -t yacy_grid_search .
# docker run -d --rm -p 8800:8800 --name yacy_grid_search yacy_grid_search
## Check if the service is running:
# curl http://localhost:8800/yacy/grid/mcp/info/status.json

# build app
FROM eclipse-temurin:8-jdk-focal AS appbuilder
COPY ./ /app
WORKDIR /app
RUN ./gradlew clean shadowDistTar

# build dist
FROM eclipse-temurin:8-jre-focal
LABEL maintainer="Michael Peter Christen <mc@yacy.net>"
ENV DEBIAN_FRONTEND noninteractive
ARG default_branch=master
COPY ./conf /app/conf/
COPY --from=appbuilder /app/build/libs/ ./app/build/libs/
WORKDIR /app
EXPOSE 8800

# for some weird reason the jar file is sometimes not named correctly
RUN if [ -e /app/build/libs/app-0.0.1-SNAPSHOT-all.jar ] ; then mv /app/build/libs/app-0.0.1-SNAPSHOT-all.jar /app/build/libs/yacy_grid_search-0.0.1-SNAPSHOT-all.jar; fi

CMD ["java", "-Xms320M", "-Xmx1G", "-jar", "/app/build/libs/yacy_grid_search-0.0.1-SNAPSHOT-all.jar"]
