package ru.robinhood.TelegramBotRobinHoodCapital.models.transaction.tonAPI;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TransactionData {
    @JsonProperty("hash")
    private String hash;
    @JsonProperty("lt")
    private long lt;
    @JsonProperty("account")
    private Account account;
    @JsonProperty("success")
    private boolean success;
    @JsonProperty("utime")
    private long utime;
    @JsonProperty("orig_status")
    private String origStatus;
    @JsonProperty("end_status")
    private String endStatus;
    @JsonProperty("total_fees")
    private int totalFees;
    @JsonProperty("end_balance")
    private long endBalance;
    @JsonProperty("transaction_type")
    private String transactionType;
    @JsonProperty("state_update_old")
    private String stateUpdateOld;
    @JsonProperty("state_update_new")
    private String stateUpdateNew;
    @JsonProperty("in_msg")
    private InMsg in_msg;
    @JsonProperty("out_msgs")
    private List<Object> outMsgs;
    @JsonProperty("block")
    private String block;
    @JsonProperty("prev_trans_hash")
    private String prevTransHash;
    @JsonProperty("prev_trans_lt")
    private long prevTransLt;
    @JsonProperty("compute_phase")
    private ComputePhase computePhase;
    @JsonProperty("storage_phase")
    private StoragePhase storagePhase;
    @JsonProperty("credit_phase")
    private CreditPhase creditPhase;
    @JsonProperty("action_phase")
    private ActionPhase actionPhase;
    @JsonProperty("aborted")
    private boolean aborted;
    @JsonProperty("destroyed")
    private boolean destroyed;
}
