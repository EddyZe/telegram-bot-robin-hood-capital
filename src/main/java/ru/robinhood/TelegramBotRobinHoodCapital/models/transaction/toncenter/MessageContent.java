package ru.robinhood.TelegramBotRobinHoodCapital.models.transaction.toncenter;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import ru.robinhood.TelegramBotRobinHoodCapital.models.transaction.toncenter.Decoded;

@Data
public class MessageContent {

    @JsonProperty("hash")
    private String hash;
    @JsonProperty("body")
    private String body;
    @JsonProperty("decoded")
    private Decoded decoded;
}
