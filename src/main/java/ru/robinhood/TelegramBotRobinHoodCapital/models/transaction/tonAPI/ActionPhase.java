package ru.robinhood.TelegramBotRobinHoodCapital.models.transaction.tonAPI;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ActionPhase {

    @JsonProperty("success")
    private boolean success;
    @JsonProperty("result_code")
    private int resultCode;
    @JsonProperty("total_actions")
    private int totalActions;
    @JsonProperty("skipped_actions")
    private int skippedActions;
    @JsonProperty("fwd_fees")
    private int fwdFees;
    @JsonProperty("total_fees")
    private int totalFees;
}
