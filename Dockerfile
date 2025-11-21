FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

COPY . .

RUN chmod +x gradlew
RUN ./gradlew build --no-daemon

CMD ["java", "-jar", "build/libs/FlowAndChannelsTest-1.0-SNAPSHOT.jar"]
