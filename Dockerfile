FROM mcr.microsoft.com/java/jre:17-zulu-alpine

WORKDIR /app

COPY ./server/build/install/server-shadow/bin /app/bin
COPY ./server/build/install/server-shadow/lib /app/lib

ENTRYPOINT ["./bin/server"]
