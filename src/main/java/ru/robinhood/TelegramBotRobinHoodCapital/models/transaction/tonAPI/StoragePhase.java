package ru.robinhood.TelegramBotRobinHoodCapital.models.transaction.tonAPI;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class StoragePhase {
    @JsonProperty("fees_collected")
    private int feesCollected;
    @JsonProperty("status_change")
    private String statusChange;
}
