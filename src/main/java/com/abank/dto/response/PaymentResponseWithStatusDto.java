package com.abank.dto.response;

import com.abank.model.PaymentStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class PaymentResponseWithStatusDto extends PaymentResponseDto {
    @JsonProperty("status")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final PaymentStatus status;

    public PaymentResponseWithStatusDto(PaymentResponseDto dto) {
        this.setPaymentId(dto.getPaymentId());
        this.status = PaymentStatus.ok;
    }

    public PaymentResponseWithStatusDto(Long paymentId) {
        super(paymentId);
        this.status = PaymentStatus.ok;
    }

    public PaymentResponseWithStatusDto(Long paymentId, PaymentStatus status) {
        super(paymentId);
        this.status = status;
    }
}
