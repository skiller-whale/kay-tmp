# Use Gradle with Java 21
FROM gradle:8-jdk21 AS build

# Set working directory
WORKDIR /app

# Copy Gradle configuration files for dependency resolution
COPY settings.gradle build.gradle ./

# Copy module build files
COPY wordlestats/build.gradle ./wordlestats/
COPY wordlestats-cli/build.gradle ./wordlestats-cli/
COPY wordlestats-tests/build.gradle ./wordlestats-tests/

# Download dependencies (for layer caching)
RUN gradle dependencies --no-daemon || true

# Copy data and CLI source code (not for editing)
COPY wordlestats-cli/src/ ./wordlestats-cli/src/
COPY data/ ./data/

# Note: wordlestats/ and wordlestats-tests/ will be mounted as volumes
# This allows learners to edit code outside the container and see changes reflected

# Test stage - builds from volume-mounted source
FROM build AS tests
RUN apt-get update && apt-get install -y inotify-tools && rm -rf /var/lib/apt/lists/*
WORKDIR /app
CMD ["gradle", "test"]

# Production stage - run the CLI app
FROM build AS cli
WORKDIR /app
CMD ["sh", "-c", "gradle :wordlestats-cli:run --quiet --console=plain"]
