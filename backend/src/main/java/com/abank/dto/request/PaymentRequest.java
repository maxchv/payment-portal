package com.abank.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class PaymentRequest {
    @Positive
    @JsonProperty(value = "source_acc_id", required = true)
    private Long sourceAccount;

    @Positive
    @JsonProperty(value = "dest_acc_id", required = true)
    private Long destinationAccount;

    @JsonProperty(value = "amount", required = true)
    @NotNull
    @Positive
    private BigDecimal amount;

    @JsonProperty("reason")
    private String reason;
}
