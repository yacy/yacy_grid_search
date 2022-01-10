## yacy_grid_search dockerfile
## examples:
# docker build -t yacy_grid_search .
# docker run -d --rm -p 8800:8800 --name yacy_grid_search yacy_grid_search
## Check if the service is running:
# curl http://localhost:8800/yacy/grid/mcp/info/status.json

# build app
FROM adoptopenjdk/openjdk8:alpine AS appbuilder
COPY ./ /app
WORKDIR /app
RUN ./gradlew assemble

# build dist
FROM adoptopenjdk/openjdk8:alpine
LABEL maintainer="Michael Peter Christen <mc@yacy.net>"
ENV DEBIAN_FRONTEND noninteractive
ARG default_branch=master
COPY ./conf /app/conf/
COPY --from=appbuilder /app/build/libs/ ./app/build/libs/
WORKDIR /app
EXPOSE 8800
CMD ["java", "-jar", "/app/build/libs/yacy_grid_search-0.0.1-SNAPSHOT-all.jar"]
