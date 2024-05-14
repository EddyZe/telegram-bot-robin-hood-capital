package ru.robinhood.TelegramBotRobinHoodCapital.models.transaction.toncenter;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


@Data
public class Action {
    @JsonProperty("valid")
    private Boolean valid;
    @JsonProperty("success")
    private Boolean success;
    @JsonProperty("no_funds")
    private Boolean noFunds;
    @JsonProperty("result_code")
    private Integer resultCode;
    @JsonProperty("tot_actions")
    private Integer totActions;
    @JsonProperty("msgs_created")
    private Integer msgsCreated;
    @JsonProperty("spec_actions")
    private Integer specActions;
    @JsonProperty("tot_msg_size")
    private TotMsgSize totMsgSize;
    @JsonProperty("status_change")
    private String statusChange;
    @JsonProperty("skipped_actions")
    private Integer skippedActions;
    @JsonProperty("action_list_hash")
    private String actionListHash;
    @JsonProperty("total_fwd_fees")
    private String totalFwdFees;
    @JsonProperty("total_action_fees")
    private String totalActionFees;
}
