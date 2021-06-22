package com.abank.service;

import com.abank.dto.request.PaymentRequestDto;
import com.abank.dto.request.PaymentRequestInfoDto;
import com.abank.dto.response.PaymentResponseDto;
import com.abank.dto.response.PaymentResponseInfoDto;
import com.abank.dto.response.PaymentResponseWithStatusDto;
import com.abank.model.Account;
import com.abank.model.Client;
import com.abank.model.Payment;
import com.abank.model.PaymentStatus;
import com.abank.repository.AccountRepository;
import com.abank.repository.PaymentRepository;
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
    public PaymentResponseDto createPayment(PaymentRequestDto paymentDto) throws AccountNotFoundException, NotEnoughMoney {
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

        return new PaymentResponseDto(payment.getId());
    }

    @Override
    public List<PaymentResponseWithStatusDto> createPayments(PaymentRequestDto[] payments) {
        List<PaymentResponseWithStatusDto> response = new ArrayList<>();
        for (PaymentRequestDto payment : payments) {
            try {
                var resp = createPayment(payment);
                response.add(new PaymentResponseWithStatusDto(resp));
            } catch (NotEnoughMoney ex) {
                response.add(new PaymentResponseWithStatusDto(ex.getPaymentId(), PaymentStatus.error));
            } catch (AccountNotFoundException e) {
                e.printStackTrace();
            }
        }
        return response;
    }

    @Override
    public List<PaymentResponseInfoDto> getPaymentInfo(PaymentRequestInfoDto paymentRequest) {
        var payment = new Payment();
        Account sourceAccount = new Account();
        Client payer = new Client();
        payer.setId(paymentRequest.getPayerId());
        sourceAccount.setClient(payer);
        sourceAccount.setId(paymentRequest.getSourceAccountId());

        Account destinationAccount = new Account();
        Client recepient = new Client();
        recepient.setId(paymentRequest.getRecipientId());
        destinationAccount.setId(paymentRequest.getDestinationAccountId());
        destinationAccount.setClient(recepient);

        payment.setSourceAccount(sourceAccount);
        payment.setDestinationAccount(destinationAccount);

        return paymentRepository
                .findAllByPayment(payment)
                .stream()
                .map(mappingUtils::paymentEntityToResponseDto)
                .collect(Collectors.toList());
    }
}
