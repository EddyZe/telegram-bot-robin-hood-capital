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
public class ComputePhase {
    @JsonProperty("skipped")
    private boolean skipped;
    @JsonProperty("success")
    private boolean success;
    @JsonProperty("gas_fees")
    private int gasFees;
    @JsonProperty("gas_used")
    private int gasUsed;
    @JsonProperty("vm_steps")
    private int vmSteps;
    @JsonProperty("exit_code")
    private int exitCode;
    @JsonProperty("exit_code_description")
    private String exitCodeDescription;
}
