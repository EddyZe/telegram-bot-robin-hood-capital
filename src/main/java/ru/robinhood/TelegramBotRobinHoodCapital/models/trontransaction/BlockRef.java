package ru.robinhood.TelegramBotRobinHoodCapital.models.trontransaction;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class BlockRef {
    @JsonProperty("workchain")
    private Integer workchain;
    @JsonProperty("shard")
    private String shard;
    @JsonProperty("seqno")
    private Integer seqno;
}
