package com.abank.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentInDto {
    @JsonProperty("source_acc_id")
    private Long sourceAccount;

    @JsonProperty("dest_acc_id")
    private Long destinationAccount;

    @JsonProperty("amount")
    private BigDecimal amount;

    @JsonProperty("reason")
    private String reason;
}