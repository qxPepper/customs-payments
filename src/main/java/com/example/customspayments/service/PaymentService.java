package com.example.customspayments.service;

import com.example.customspayments.Entity.Payment;
import com.example.customspayments.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    public List<Payment> paymentsAll() {
        return paymentRepository.findAll();
    }

    public Payment getPaymentById(Long id) {
        Optional<Payment> payment = paymentRepository.findById(id);
        return payment.orElse(null);
    }

    public Payment getPaymentByRecordNumber(String recordNumber) {
        Optional<Payment> payment = paymentRepository.getPaymentByRecordNumber(recordNumber);
        return payment.orElse(null);
    }

}
