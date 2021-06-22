package com.abank.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class PaymentRequestDto {
    @JsonProperty("source_acc_id")
    private Long sourceAccount;

    @JsonProperty("dest_acc_id")
    private Long destinationAccount;

    @JsonProperty("amount")
    private BigDecimal amount;

    @JsonProperty("reason")
    private String reason;
}
