package com.abank.dto.response;

import com.abank.model.PaymentStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class PaymentResponseWithStatus extends PaymentResponse {
    @JsonProperty("status")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final PaymentStatus status;

    public PaymentResponseWithStatus(PaymentResponse dto) {
        this.setPaymentId(dto.getPaymentId());
        this.status = PaymentStatus.ok;
    }

    public PaymentResponseWithStatus(Long paymentId, PaymentStatus status) {
        super(paymentId);
        this.status = status;
    }
}
