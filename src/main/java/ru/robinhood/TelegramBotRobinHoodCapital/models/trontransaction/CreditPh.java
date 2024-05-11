package ru.robinhood.TelegramBotRobinHoodCapital.models.trontransaction;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CreditPh {
    @JsonProperty("credit")
    private String credit;
    @JsonProperty("due_fees_collected")
    private String dueFeesCollected;
}
