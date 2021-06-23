package com.abank.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.OneToMany;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class PaymentRequestDto {
    @JsonProperty("source_acc_id")
    @Positive
    private Long sourceAccount;

    @JsonProperty("dest_acc_id")
    @Positive
    private Long destinationAccount;

    @JsonProperty("amount")
    @OneToMany
    private BigDecimal amount;

    @JsonProperty("reason")
    private String reason;
}
