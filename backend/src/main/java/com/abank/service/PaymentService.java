package com.abank.service;

import com.abank.dto.request.PaymentRequest;
import com.abank.dto.request.PaymentRequestInfo;
import com.abank.dto.response.PaymentResponse;
import com.abank.dto.response.PaymentResponseInfo;
import com.abank.dto.response.PaymentResponseWithStatus;
import com.abank.model.Payment;

import java.util.List;

public interface PaymentService {
    PaymentResponse createPayment(PaymentRequest payment) throws AccountNotFoundException, NotEnoughMoney;

    List<PaymentResponseWithStatus> createPayments(PaymentRequest[] payments);

    List<PaymentResponseInfo> getPaymentInfo(PaymentRequestInfo paymentRequest);

    List<Payment> findAll();
}
