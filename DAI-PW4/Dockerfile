# The `FROM` instruction specifies the base image. You are extending the `eclipse-temurin:17` image.
FROM eclipse-temurin:17

# The `WORKDIR` instruction is a directory inside the container where the application will be copied.
WORKDIR /app

# The `COPY` instruction copies the application from the host to the container.
COPY target/DAI-PW4-1.0-SNAPSHOT.jar /app/DAI-PW4-1.0-SNAPSHOT.jar

# The `ENTRYPOINT` instruction specifies the command to run when the container starts.
ENTRYPOINT ["java", "-jar", "DAI-PW4-1.0-SNAPSHOT.jar"]
