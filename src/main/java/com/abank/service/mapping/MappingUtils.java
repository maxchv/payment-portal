package com.abank.service.mapping;

import com.abank.dto.response.PaymentResponseInfoDto;
import com.abank.model.Payment;

public interface MappingUtils {
    PaymentResponseInfoDto paymentEntityToResponseDto(Payment payment);
}
