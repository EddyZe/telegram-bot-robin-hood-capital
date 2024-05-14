package ru.robinhood.TelegramBotRobinHoodCapital.models.transaction.toncenter;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AccountState {
    @JsonProperty("hash")
    private String hash;
    @JsonProperty("balance")
    private String balance;
    @JsonProperty("account_status")
    private String accountStatus;
    @JsonProperty("frozen_hash")
    private Object frozenHash;
    @JsonProperty("code_hash")
    private String codeHash;
    @JsonProperty("data_hash")
    private String dataHash;
}
