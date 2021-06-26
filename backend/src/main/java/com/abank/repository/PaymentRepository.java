package com.abank.repository;

import com.abank.model.Payment;

import java.util.List;

public interface PaymentRepository extends Repository<Payment, Long> {
   List<Payment> findAllByPayment(Payment example);
}
