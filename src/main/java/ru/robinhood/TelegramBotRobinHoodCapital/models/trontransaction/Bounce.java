package ru.robinhood.TelegramBotRobinHoodCapital.models.trontransaction;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class Bounce {

    @JsonProperty("type")
    private String type;
}
