package ru.robinhood.TelegramBotRobinHoodCapital.models.transaction;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Decoded {

    @JsonProperty("type")
    private String type;
    @JsonProperty("comment")
    private String comment;
}
