package ru.robinhood.TelegramBotRobinHoodCapital.models.trontransaction;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class MessageContent {

    @JsonProperty("hash")
    private String hash;
    @JsonProperty("body")
    private String body;
    @JsonProperty("decoded")
    private Decoded decoded;
}
