package com.abank.service;

import com.abank.dto.PaymentInDto;
import com.abank.dto.PaymentOutDto;
import com.abank.model.Account;
import com.abank.model.Payment;
import com.abank.repository.AccountRepository;
import com.abank.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final AccountRepository accountRepository;

    public PaymentServiceImpl(PaymentRepository paymentRepository, AccountRepository accountRepository) {
        this.paymentRepository = paymentRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    public PaymentOutDto createPayment(PaymentInDto paymentDto) throws AccountNotFoundException, NotEnoughMoney {
        Optional<Account> sourceAccount = accountRepository.findById(paymentDto.getSourceAccount());
        Optional<Account> destinationAccount = accountRepository.findById(paymentDto.getDestinationAccount());
        if (sourceAccount.isEmpty() || destinationAccount.isEmpty()) {
            throw new AccountNotFoundException();
        }
        Account source = sourceAccount.get();
        Account destination = destinationAccount.get();
        if (paymentDto.getAmount().compareTo(source.getBalance()) > 0) {
            throw new NotEnoughMoney();
        }
        Payment payment = new Payment();
        payment.setSourceAccount(source);
        payment.setDestinationAccount(destination);
        payment.setAmount(paymentDto.getAmount());
        payment.setReason(payment.getReason());
        paymentRepository.save(payment);
        return new PaymentOutDto(payment.getId());
    }
}
