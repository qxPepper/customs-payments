package com.example.customspayments.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "Record Number", length = 12, unique = true, nullable = false)
    private String recordNumber;

    @Column(name = "Payment ID", length = 50, unique = true, nullable = false)
    private String paymentId;

    @Column(name = "Company Name", length = 64, nullable = false)
    private String companyName;

    @Column(name = "Payer INN", length = 12, nullable = false)
    private String payerINN;

    @Column(name = "Amount", length = 12, scale = 2, nullable = false)
    private String amount;

    @Column(name = "Status", nullable = false)
    private int status;

    public Payment(String recordNumber, String paymentId, String companyName, String payerINN, String amount, int status) {
        this.recordNumber = recordNumber;
        this.paymentId = paymentId;
        this.companyName = companyName;
        this.payerINN = payerINN;
        this.amount = amount;
        this.status = status;
    }
}
