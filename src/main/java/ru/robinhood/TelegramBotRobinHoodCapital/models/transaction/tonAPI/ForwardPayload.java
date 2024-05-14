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
public class ForwardPayload {
    @JsonProperty("is_right")
    private boolean isRight;
    @JsonProperty("value")
    private Value value;
}
