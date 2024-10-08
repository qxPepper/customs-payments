package com.example.customspayments.controller;

import com.example.customspayments.Entity.Payment;
import com.example.customspayments.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/payments/")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @GetMapping(value = "all")
    public List<Payment> paymentsAll() {
        return paymentService.paymentsAll();
    }

    @GetMapping(value = "id/{id}")
    public Payment getPaymentById(@PathVariable Long id) {
        return paymentService.getPaymentById(id);
    }

    @GetMapping(value = "recordnumber/{recordNumber}")
    public Payment getPaymentByRecordNumber(@PathVariable String recordNumber) {
        return paymentService.getPaymentByRecordNumber(recordNumber);
    }
}
