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
public class DecodedBody {
    @JsonProperty("text")
    private String text;
    @JsonProperty("query_id")
    private long queryId;
    @JsonProperty("amount")
    private String amount;
    @JsonProperty("sender")
    private String sender;
    @JsonProperty("forward_payload")
    private ForwardPayload forwardPayload;
}
