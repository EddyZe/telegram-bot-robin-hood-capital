FROM openjdk:21
COPY ./target/telegram-bot-robin-hood-capital-0.0.1-SNAPSHOT.jar telegram-bot-robin-hood-capital-0.0.1-SNAPSHOT.jar
EXPOSE 8080
CMD ["java", "-jar", "telegram-bot-robin-hood-capital-0.0.1-SNAPSHOT.jar"]