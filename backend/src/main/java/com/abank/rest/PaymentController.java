package com.abank.rest;

import com.abank.dto.request.PaymentRequest;
import com.abank.dto.request.PaymentRequestInfo;
import com.abank.dto.response.ErrorResponse;
import com.abank.dto.response.PaymentResponse;
import com.abank.dto.response.PaymentResponseInfo;
import com.abank.dto.response.PaymentResponseWithStatus;
import com.abank.model.Payment;
import com.abank.service.PaymentService;
import com.abank.service.error.AccountNotFoundException;
import com.abank.service.error.NotEnoughMoney;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(path = "/api/v1")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping(value = "/payment",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<PaymentResponse> createPayment(@Valid @RequestBody PaymentRequest payment)
            throws NotEnoughMoney, AccountNotFoundException {
        return ResponseEntity.status(HttpStatus.CREATED).body(paymentService.createPayment(payment));
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({AccountNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleAccountNotFoundExceptions(AccountNotFoundException ex) {
        return ResponseEntity.internalServerError().body(new ErrorResponse("account", "Not found"));
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({NotEnoughMoney.class})
    public ResponseEntity<ErrorResponse> handleNotEnoughMoneyExceptions(NotEnoughMoney ex) {
        return ResponseEntity.internalServerError().body(new ErrorResponse("balance", "Not enough money"));
    }

    @GetMapping(value = "/payments",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<List<Payment>> getPayments() {
        return ResponseEntity.ok(paymentService.findAll());
    }

    @PostMapping(value = "/payments",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<List<PaymentResponseWithStatus>> createPayments(@Valid @RequestBody PaymentRequest[] payments) {
        return ResponseEntity.ok(paymentService.createPayments(payments));
    }

    @PostMapping(value = "/payments/info",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<List<PaymentResponseInfo>> paymentInfo(@Valid @RequestBody PaymentRequestInfo paymentRequestInfo) {
        return ResponseEntity.ok(paymentService.getPaymentInfo(paymentRequestInfo));
    }
}
