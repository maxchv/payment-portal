package com.abank.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.Positive;

@Data
public class PaymentRequestInfo {
    @JsonProperty(value = "payer_id", required = true)
    @Positive
    private Long payerId;

    @JsonProperty(value = "recipient_id", required = true)
    @Positive
    private Long recipientId;

    @JsonProperty(value = "source_acc_id", required = true)
    @Positive
    private Long sourceAccountId;

    @JsonProperty(value = "dest_acc_id", required = true)
    @Positive
    private Long destinationAccountId;
}
