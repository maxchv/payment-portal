package com.abank.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class PaymentResponseInfo {
    @JsonProperty("payment_id")
    @Positive
    private Long paymentId;

    @JsonProperty("timestamp")
    private LocalDateTime timestamp;

    @JsonProperty("src_acc_num")
    private String sourceAccountNumber;

    @JsonProperty("dest_acc_num")
    private String destinationAccountNumber;

    @JsonProperty("amount")
    @Positive
    private BigDecimal amount;

    @AllArgsConstructor
    @Data
    public static class ClientFullName {
        @JsonProperty("first_name")
        @NotNull
        @NotBlank
        private String firstName;

        @JsonProperty("last_name")
        @NotNull
        @NotBlank
        private String lastName;
    }

    @JsonProperty("payer")
    @NotNull
    private ClientFullName payer;

    @JsonProperty("recipient")
    @NotNull
    private ClientFullName recipient;
}
