package com.abank.service;

import com.abank.dto.PaymentInDto;
import com.abank.dto.PaymentOutDto;
import com.abank.dto.PaymentOutWithStatusDto;

import java.util.List;

public interface PaymentService {
    PaymentOutDto createPayment(PaymentInDto payment) throws AccountNotFoundException, NotEnoughMoney;

    List<PaymentOutWithStatusDto> createPayments(PaymentInDto[] payments);
}
