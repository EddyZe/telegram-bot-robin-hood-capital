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
public class TextValue {
    @JsonProperty("text")
    private String text;
}
