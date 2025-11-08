FROM azul/zulu-openjdk-alpine:21-jre

WORKDIR /app

# Create directory for persistent data
RUN mkdir -p /app/data

# Copy application files
COPY ./server/build/install/server-shadow/bin /app/bin
COPY ./server/build/install/server-shadow/lib /app/lib

# Define volume for database persistence
VOLUME ["/app/data"]

# Set default database URL to use the persistent volume
ENV DATABASE_URL="jdbc:sqlite:/app/data/gizmopoker.db"

ENTRYPOINT ["./bin/server"]

