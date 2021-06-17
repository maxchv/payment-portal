package com.abank.repository.jpa;

import com.abank.model.Payment;
import com.abank.repository.PaymentRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

@Profile("jpa")
public interface JpaPaymentRepository extends JpaRepository<Payment, Long>, PaymentRepository {
}
