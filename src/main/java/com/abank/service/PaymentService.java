package com.abank.service;

import com.abank.dto.*;

import java.util.List;

public interface PaymentService {
    PaymentOutDto createPayment(PaymentInDto payment) throws AccountNotFoundException, NotEnoughMoney;

    List<PaymentOutWithStatusDto> createPayments(PaymentInDto[] payments);

    List<PaymentResponseInfoDto> getPaymentInfo(PaymentRequestInfoDto paymentRequest);
}
