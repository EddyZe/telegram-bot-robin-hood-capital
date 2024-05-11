package ru.robinhood.TelegramBotRobinHoodCapital.models.trontransaction;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


@Data
public class Description {
    @JsonProperty("action")
    private Action action;
    @JsonProperty("bounce")
    private Bounce bounce;
    @JsonProperty("aborted")
    private Boolean aborted;
    @JsonProperty("credit_ph")
    private CreditPh creditPh;
    @JsonProperty("destroyed")
    private Boolean destroyed;
    @JsonProperty("compute_ph")
    private ComputePh computePh;
    @JsonProperty("storage_ph")
    private StoragePh storagePh;
    @JsonProperty("credit_first")
    private Boolean creditFirst;
}
