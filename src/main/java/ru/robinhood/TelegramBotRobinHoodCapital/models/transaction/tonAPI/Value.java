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
public class Value {
    @JsonProperty("sum_type")
    private String sumType;
    @JsonProperty("op_code")
    private int opCode;
    @JsonProperty("value")
    private TextValue value;
}
