package com.abank.service;

import com.abank.dto.PaymentInDto;
import com.abank.dto.PaymentOutDto;
import com.abank.dto.PaymentOutWithStatusDto;
import com.abank.model.Account;
import com.abank.model.Payment;
import com.abank.model.PaymentStatus;
import com.abank.repository.AccountRepository;
import com.abank.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
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
    @Transactional
    public PaymentOutDto createPayment(PaymentInDto paymentDto) throws AccountNotFoundException, NotEnoughMoney {
        Optional<Account> sourceAccount = accountRepository.findById(paymentDto.getSourceAccount());
        Optional<Account> destinationAccount = accountRepository.findById(paymentDto.getDestinationAccount());
        if (sourceAccount.isEmpty() || destinationAccount.isEmpty()) {
            throw new AccountNotFoundException();
        }
        Account source = sourceAccount.get();
        Account destination = destinationAccount.get();

        Payment payment = new Payment();
        payment.setSourceAccount(source);
        payment.setDestinationAccount(destination);
        payment.setAmount(paymentDto.getAmount());
        payment.setReason(paymentDto.getReason());

        BigDecimal sourceBalance = source.getBalance();
        if (paymentDto.getAmount().compareTo(sourceBalance) <= 0) {
            BigDecimal destinationBalance = destination.getBalance();
            source.setBalance(sourceBalance.subtract(payment.getAmount()));
            destination.setBalance(destinationBalance.add(payment.getAmount()));
            accountRepository.save(source);
            accountRepository.save(destination);
            payment.setStatus(PaymentStatus.ok);
            paymentRepository.save(payment);
        } else {
            payment.setStatus(PaymentStatus.error);
            paymentRepository.save(payment);
            throw new NotEnoughMoney(payment.getId());
        }

        return new PaymentOutDto(payment.getId());
    }

    @Override
    public List<PaymentOutWithStatusDto> createPayments(PaymentInDto[] payments) {
        List<PaymentOutWithStatusDto> response = new ArrayList<>();
        for (PaymentInDto payment : payments) {
            try {
                var resp = createPayment(payment);
                response.add(new PaymentOutWithStatusDto(resp));
            } catch (NotEnoughMoney ex) {
                response.add(new PaymentOutWithStatusDto(ex.getPaymentId(), PaymentStatus.error));
            } catch (AccountNotFoundException e) {
                e.printStackTrace();
            }
        }
        return response;
    }
}
