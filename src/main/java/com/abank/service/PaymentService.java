package com.abank.service;

import com.abank.dto.PaymentInDto;
import com.abank.dto.PaymentOutDto;

public interface PaymentService {
    PaymentOutDto createPayment(PaymentInDto payment) throws AccountNotFoundException, NotEnoughMoney;
}
