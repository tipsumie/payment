package com.example.payment_demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class PaymentDemoController {
//    @GetMapping("/payments")
//    public List<Payment> getOrders(@RequestHeader Map<String, String> RequestHeader) {
//        log.info("request {}", RequestHeader);
//        return Arrays.asList(
//                new Payment(1L, "book", 1234.00),
//                new Payment(2L, "cars", 1224.00),
//                new Payment(3L, "disk", 1244.00),
//                new Payment(4L, "laptop", 1500.00));
//    }
}
