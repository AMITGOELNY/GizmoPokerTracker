FROM https://hub.docker.com/layers/azul/zulu-openjdk-alpine/21-jre-headless/images/sha256-1c7c6c8af2442f2a2d92c8ca62056bd1b3ef107da07db80ca4e24507c8c89fb9

WORKDIR /app

COPY ./server/build/install/server-shadow/bin /app/bin
COPY ./server/build/install/server-shadow/lib /app/lib

ENTRYPOINT ["./bin/server"]
