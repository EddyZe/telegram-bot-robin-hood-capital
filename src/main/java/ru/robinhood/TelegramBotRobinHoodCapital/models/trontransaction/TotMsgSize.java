package ru.robinhood.TelegramBotRobinHoodCapital.models.trontransaction;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TotMsgSize {

    @JsonProperty("bits")
    private String bits;
    @JsonProperty("cells")
    private String cells;
}
