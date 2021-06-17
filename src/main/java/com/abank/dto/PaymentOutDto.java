package com.abank.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentOutDto {
    @JsonProperty("payment_id")
    private Long paymentId;
}
