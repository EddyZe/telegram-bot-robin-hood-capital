package ru.robinhood.TelegramBotRobinHoodCapital.models.trontransaction;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;


@Data
public class Transaction {
    @JsonProperty("account")
    private String account;
    @JsonProperty("hash")
    private String hash;
    @JsonProperty("lt")
    private String lt;
    @JsonProperty("now")
    private Integer now;
    @JsonProperty("orig_status")
    private String origStatus;
    @JsonProperty("end_status")
    private String endStatus;
    @JsonProperty("total_fees")
    private String totalFees;
    @JsonProperty("prev_trans_hash")
    private String prevTransHash;
    @JsonProperty("prev_trans_lt")
    private String prevTransLt;
    @JsonProperty("description")
    private Description description;
    @JsonProperty("block_ref")
    private BlockRef blockRef;
    @JsonProperty("in_msg")
    private InMsg inMsg;
    @JsonProperty("out_msgs")
    private List<OutMsg> outMsgs;
    @JsonProperty("account_state_before")
    private AccountState accountStateBefore;
    @JsonProperty("account_state_after")
    private AccountState accountStateAfter;
    @JsonProperty("mc_block_seqno")
    private Integer mcBlockSeqno;

    // Геттеры и сеттеры
}
