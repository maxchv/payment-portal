package com.abank.service;

import com.abank.dto.request.PaymentRequestDto;
import com.abank.dto.request.PaymentRequestInfoDto;
import com.abank.dto.response.PaymentResponseDto;
import com.abank.dto.response.PaymentResponseInfoDto;
import com.abank.dto.response.PaymentResponseWithStatusDto;

import java.util.List;

public interface PaymentService {
    PaymentResponseDto createPayment(PaymentRequestDto payment) throws AccountNotFoundException, NotEnoughMoney;

    List<PaymentResponseWithStatusDto> createPayments(PaymentRequestDto[] payments);

    List<PaymentResponseInfoDto> getPaymentInfo(PaymentRequestInfoDto paymentRequest);
}
