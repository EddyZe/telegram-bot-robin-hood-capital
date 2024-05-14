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
public class Account {
    @JsonProperty("address")
    private String address;
    @JsonProperty("is_scam")
    private boolean isScam;
    @JsonProperty("is_wallet")
    private boolean isWallet;
}
