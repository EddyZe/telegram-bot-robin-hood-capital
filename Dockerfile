FROM openjdk:22
COPY /target/telegram-bot-robin-hood-capital-0.0.1-SNAPSHOT.jar telegram-bot-robin-hood-capital-0.0.1-SNAPSHOT.jar
EXPOSE 8080
VOLUME /data/
CMD ["java", "-jar", "telegram-bot-robin-hood-capital-0.0.1-SNAPSHOT.jar"]