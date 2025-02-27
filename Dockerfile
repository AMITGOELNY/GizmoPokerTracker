FROM azul/zulu-openjdk-alpine:21-jre

WORKDIR /app

COPY ./server/build/install/server-shadow/bin /app/bin
COPY ./server/build/install/server-shadow/lib /app/lib

ENTRYPOINT ["./bin/server"]

