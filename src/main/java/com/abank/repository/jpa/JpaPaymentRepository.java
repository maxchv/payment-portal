package com.abank.repository.jpa;

import com.abank.model.Payment;
import com.abank.repository.PaymentRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Profile("jpa")
public interface JpaPaymentRepository extends JpaRepository<Payment, Long>, PaymentRepository {

    default List<Payment> findAllByPayment(Payment example) {
        return findAll(Example.of(example));
    }

}
