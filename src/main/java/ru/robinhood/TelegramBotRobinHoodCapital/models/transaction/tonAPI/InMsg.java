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
public class InMsg {

    @JsonProperty("msg_type")
    private String msgType;
    @JsonProperty("created_lt")
    private long createdLt;
    @JsonProperty("ihr_disabled")
    private boolean ihrDisabled;
    @JsonProperty("bounce")
    private boolean bounce;
    @JsonProperty("bounced")
    private boolean bounced;
    @JsonProperty("value")
    private int value;
    @JsonProperty("fwd_fee")
    private int fwdFee;
    @JsonProperty("ihr_fee")
    private int ihrFee;
    @JsonProperty("destination")
    private Destination destination;
    @JsonProperty("source")
    private Source source;
    @JsonProperty("import_fee")
    private int importFee;
    @JsonProperty("created_at")
    private long createdAt;
    @JsonProperty("op_code")
    private String opCode;
    @JsonProperty("raw_body")
    private String rawBody;
    @JsonProperty("decoded_op_name")
    private String decodedOpName;
    @JsonProperty("decoded_body")
    private DecodedBody decodedBody;
}
