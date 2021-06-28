package com.abank.service.impl;

import com.abank.dto.request.PaymentRequest;
import com.abank.dto.request.PaymentRequestInfo;
import com.abank.dto.response.PaymentResponse;
import com.abank.dto.response.PaymentResponseInfo;
import com.abank.dto.response.PaymentResponseWithStatus;
import com.abank.model.Account;
import com.abank.model.Client;
import com.abank.model.Payment;
import com.abank.model.PaymentStatus;
import com.abank.repository.AccountRepository;
import com.abank.repository.PaymentRepository;
import com.abank.service.AccountNotFoundException;
import com.abank.service.NotEnoughMoney;
import com.abank.service.PaymentService;
import com.abank.service.mapping.MappingUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final AccountRepository accountRepository;
    private final MappingUtils mappingUtils;

    public PaymentServiceImpl(PaymentRepository paymentRepository,
                              AccountRepository accountRepository,
                              MappingUtils mappingUtils) {
        this.paymentRepository = paymentRepository;
        this.accountRepository = accountRepository;
        this.mappingUtils = mappingUtils;
    }

    @Override
    @Transactional
    public PaymentResponse createPayment(PaymentRequest paymentDto) throws AccountNotFoundException, NotEnoughMoney {
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
            accountRepository.updateBalance(source.getId(), source.getBalance());
            accountRepository.updateBalance(destination.getId(), destination.getBalance());
            payment.setStatus(PaymentStatus.ok);
            paymentRepository.save(payment);
        } else {
            payment.setStatus(PaymentStatus.error);
            paymentRepository.save(payment);
            throw new NotEnoughMoney(payment.getId());
        }

        return new PaymentResponse(payment.getId());
    }

    @Override
    public List<PaymentResponseWithStatus> createPayments(PaymentRequest[] payments) {
        List<PaymentResponseWithStatus> response = new ArrayList<>();
        for (PaymentRequest payment : payments) {
            try {
                var resp = createPayment(payment);
                response.add(new PaymentResponseWithStatus(resp));
            } catch (NotEnoughMoney ex) {
                response.add(new PaymentResponseWithStatus(ex.getPaymentId(), PaymentStatus.error));
            } catch (AccountNotFoundException e) {
                e.printStackTrace();
            }
        }
        return response;
    }

    @Override
    public List<PaymentResponseInfo> getPaymentInfo(PaymentRequestInfo paymentRequest) {
        var payment = new Payment();
        Account sourceAccount = new Account();
        Client payer = new Client();
        payer.setId(paymentRequest.getPayerId());
        sourceAccount.setClient(payer);
        sourceAccount.setId(paymentRequest.getSourceAccountId());

        Account destinationAccount = new Account();
        Client recipient = new Client();
        recipient.setId(paymentRequest.getRecipientId());
        destinationAccount.setId(paymentRequest.getDestinationAccountId());
        destinationAccount.setClient(recipient);

        payment.setSourceAccount(sourceAccount);
        payment.setDestinationAccount(destinationAccount);

        return paymentRepository
                .findAllByPayment(payment)
                .stream()
                .map(mappingUtils::paymentEntityToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<Payment> findAll() {
        return paymentRepository.findAll();
    }
}
