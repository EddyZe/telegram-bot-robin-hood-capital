package ru.robinhood.TelegramBotRobinHoodCapital.models.trontransaction;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class StoragePh {

    @JsonProperty("status_change")
    private String statusChange;
    @JsonProperty("storage_fees_collected")
    private String storageFeesCollected;

}
