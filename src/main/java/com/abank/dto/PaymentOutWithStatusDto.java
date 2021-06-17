package com.abank.dto;

import com.abank.model.PaymentStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class PaymentOutWithStatusDto extends PaymentOutDto {
    @JsonProperty("status")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final PaymentStatus status;

    public PaymentOutWithStatusDto(PaymentOutDto dto) {
        this.setPaymentId(dto.getPaymentId());
        this.status = PaymentStatus.ok;
    }

    public PaymentOutWithStatusDto(Long paymentId) {
        super(paymentId);
        this.status = PaymentStatus.ok;
    }

    public PaymentOutWithStatusDto(Long paymentId, PaymentStatus status) {
        super(paymentId);
        this.status = status;
    }
}
