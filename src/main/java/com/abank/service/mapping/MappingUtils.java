package com.abank.service.mapping;

import com.abank.dto.PaymentResponseInfoDto;
import com.abank.model.Payment;

public interface MappingUtils {
    PaymentResponseInfoDto paymentEntityToResponseDto(Payment payment);
}
