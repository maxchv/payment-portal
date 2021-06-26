package com.abank.service.mapping;

import com.abank.dto.response.PaymentResponseInfo;
import com.abank.model.Payment;
import org.springframework.stereotype.Service;

import static com.abank.dto.response.PaymentResponseInfo.ClientFullName;

@Service
public class MappingUtilsImpl implements MappingUtils {
    @Override
    public PaymentResponseInfo paymentEntityToResponseDto(Payment payment) {
        var payer = payment.getSourceAccount().getClient();
        var recipient = payment.getDestinationAccount().getClient();
        return PaymentResponseInfo
                .builder()
                .paymentId(payment.getId())
                .timestamp(payment.getTimestamp())
                .sourceAccountNumber(payment.getSourceAccount().getAccountNum())
                .destinationAccountNumber(payment.getDestinationAccount().getAccountNum())
                .amount(payment.getAmount())
                .payer(new ClientFullName(payer.getFirstName(), payer.getLastName()))
                .recipient(new ClientFullName(recipient.getFirstName(), recipient.getLastName()))
                .build();
    }
}
