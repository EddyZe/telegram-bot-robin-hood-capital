package ru.robinhood.TelegramBotRobinHoodCapital.models.transaction;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ComputePh {

    @JsonProperty("mode")
    private Integer mode;
    @JsonProperty("type")
    private String type;
    @JsonProperty("success")
    private Boolean success;
    @JsonProperty("gas_fees")
    private String gasFees;
    @JsonProperty("gas_used")
    private String gasUsed;
    @JsonProperty("vm_steps")
    private Integer vmSteps;
    @JsonProperty("exit_code")
    private Integer exitCode;
    @JsonProperty("gas_limit")
    private String gasLimit;
    @JsonProperty("msg_state_used")
    private Boolean msgStateUsed;
    @JsonProperty("account_activated")
    private Boolean accountActivated;
    @JsonProperty("vm_init_state_hash")
    private String vmInitStateHash;
    @JsonProperty("vm_final_state_hash")
    private String vmFinalStateHash;
}
