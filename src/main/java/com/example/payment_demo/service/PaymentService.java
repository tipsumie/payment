package com.example.payment_demo.service;

import com.example.payment_demo.model.PaymentRequest;
import com.example.payment_demo.model.PaymentResponse;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PaymentService {

    public PaymentResponse processPayment(PaymentRequest paymentRequest) {
        // Process the payment (mocked logic here)
        PaymentResponse response = new PaymentResponse();
        response.setStatus("SUCCESS");
        response.setMessage("Payment processed successfully.");
        response.setTransactionId(UUID.randomUUID().toString());
        
        return response;
    }
}
