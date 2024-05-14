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
    private long resultCode;
    @JsonProperty("total_actions")
    private long totalActions;
    @JsonProperty("skipped_actions")
    private long skippedActions;
    @JsonProperty("fwd_fees")
    private long fwdFees;
    @JsonProperty("total_fees")
    private long totalFees;
}
