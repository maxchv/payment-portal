package com.abank.service.mapping;

import com.abank.dto.PaymentResponseInfoDto;
import com.abank.model.Payment;
import org.springframework.stereotype.Service;

import static com.abank.dto.PaymentResponseInfoDto.Client;

@Service
public class MappingUtilsImpl implements MappingUtils {
    @Override
    public PaymentResponseInfoDto paymentEntityToResponseDto(Payment payment) {
        var payer = payment.getSourceAccount().getClient();
        var recipient = payment.getDestinationAccount().getClient();
        return PaymentResponseInfoDto
                .builder()
                .paymentId(payment.getId())
                .timestamp(payment.getTimestamp())
                .sourceAccountNumber(payment.getSourceAccount().getAccountNum())
                .destinationAccountNumber(payment.getDestinationAccount().getAccountNum())
                .amount(payment.getAmount())
                .payer(new Client(payer.getFirstName(), payer.getLastName()))
                .recipient(new Client(recipient.getFirstName(), recipient.getLastName()))
                .build();
    }
}
