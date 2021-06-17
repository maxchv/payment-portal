package com.abank.rest;

import com.abank.dto.PaymentInDto;
import com.abank.dto.PaymentOutDto;
import com.abank.service.AccountNotFoundException;
import com.abank.service.NotEnoughMoney;
import com.abank.service.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping(value = "payment", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<PaymentOutDto> createPayment(@RequestBody PaymentInDto payment) {
        PaymentOutDto created;
        try {
            created = paymentService.createPayment(payment);
        } catch (AccountNotFoundException | NotEnoughMoney notEnoughMoney) {
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
}
