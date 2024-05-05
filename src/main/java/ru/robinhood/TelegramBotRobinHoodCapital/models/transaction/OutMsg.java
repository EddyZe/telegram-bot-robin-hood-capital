package ru.robinhood.TelegramBotRobinHoodCapital.models.transaction;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class OutMsg {

    @JsonProperty("hash")
    private String hash;
    @JsonProperty("source")
    private String source;
    @JsonProperty("destination")
    private String destination;
    @JsonProperty("value")
    private String value;
    @JsonProperty("fwd_fee")
    private String fwdFee;
    @JsonProperty("ihr_fee")
    private String ihrFee;
    @JsonProperty("created_lt")
    private String createdLt;
    @JsonProperty("created_at")
    private String createdAt;
    @JsonProperty("opcode")
    private String opcode;
    @JsonProperty("ihr_disabled")
    private Boolean ihrDisabled;
    @JsonProperty("bounce")
    private Boolean bounce;
    @JsonProperty("bounced")
    private Boolean bounced;
    @JsonProperty("import_fee")
    private Object importFee;
    @JsonProperty("message_content")
    private MessageContent messageContent;
    @JsonProperty("init_state")
    private Object initState;
}
