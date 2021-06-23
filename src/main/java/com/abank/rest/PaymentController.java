package com.abank.rest;

import com.abank.dto.request.PaymentRequest;
import com.abank.dto.request.PaymentRequestInfo;
import com.abank.dto.response.PaymentResponse;
import com.abank.dto.response.PaymentResponseInfo;
import com.abank.dto.response.PaymentResponseWithStatus;
import com.abank.service.AccountNotFoundException;
import com.abank.service.NotEnoughMoney;
import com.abank.service.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping(value = "payment",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<PaymentResponse> createPayment(@Validated @RequestBody PaymentRequest payment,
                                                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.internalServerError().build();
        }
        PaymentResponse created;
        try {
            created = paymentService.createPayment(payment);
        } catch (AccountNotFoundException | NotEnoughMoney notEnoughMoney) {
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PostMapping(value = "payments",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<List<PaymentResponseWithStatus>> createPayments(@Validated @RequestBody PaymentRequest[] payments) {
        List<PaymentResponseWithStatus> response = paymentService.createPayments(payments);
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "payments/info",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<List<PaymentResponseInfo>> paymentInfo(@Validated @RequestBody PaymentRequestInfo paymentRequestInfo,
                                                                 BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.internalServerError().build();
        }

        List<PaymentResponseInfo> infos = paymentService.getPaymentInfo(paymentRequestInfo);

        return ResponseEntity.ok(infos);
    }
}
