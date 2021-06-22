package com.abank.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class PaymentResponseInfoDto {
    @JsonProperty("payment_id")
    private Long paymentId;

    @JsonProperty("timestamp")
    private LocalDateTime timestamp;

    @JsonProperty("src_acc_num")
    private String sourceAccountNumber;

    @JsonProperty("dest_acc_num")
    private String destinationAccountNumber;

    @JsonProperty("amount")
    private BigDecimal amount;

    @AllArgsConstructor
    @Data
    public static class Client {
        @JsonProperty("first_name")
        private String firstName;

        @JsonProperty("last_name")
        private String lastName;
    }

    @JsonProperty("payer")
    private Client payer;

    @JsonProperty("recipient")
    private Client recipient;
}
