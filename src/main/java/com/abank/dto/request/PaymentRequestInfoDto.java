package com.abank.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.Positive;

@Data
public class PaymentRequestInfoDto {
    @JsonProperty("payer_id")
    @Positive
    private Long payerId;

    @JsonProperty("recipient_id")
    @Positive
    private Long recipientId;

    @JsonProperty("source_acc_id")
    @Positive
    private Long sourceAccountId;

    @JsonProperty("dest_acc_id")
    @Positive
    private Long destinationAccountId;
}
