package com.example.payment_demo.controller;

import com.example.payment_demo.constants.Constants;
import com.example.payment_demo.model.PaymentRequest;
import com.example.payment_demo.model.PaymentResponse;
import com.example.payment_demo.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@Slf4j
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/submit")
    public ResponseEntity<PaymentResponse> submitPayment(@RequestBody PaymentRequest paymentRequest,
                                                         @RequestHeader(value = Constants.CORRELATION_ID, required = false) String correlationId) {

        log.info("Received payment request: {}", paymentRequest);

        // Process the payment
        PaymentResponse paymentResponse = paymentService.processPayment(paymentRequest);

        log.info("Payment response: {}", paymentResponse);

        return ResponseEntity.ok(paymentResponse);
    }
}
