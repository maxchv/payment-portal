package com.abank.service.mapping;

import com.abank.dto.response.PaymentResponseInfo;
import com.abank.model.Payment;

public interface MappingUtils {
    PaymentResponseInfo paymentEntityToResponseDto(Payment payment);
}
