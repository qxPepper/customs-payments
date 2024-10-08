package com.example.customspayments.repository;

import com.example.customspayments.Entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    @Query(value = "select p from Payment p where p.recordNumber=:recordNumber")
    Optional<Payment> getPaymentByRecordNumber(@Param("recordNumber") String recordNumber);

    // по аналогии можно сделать поиск по любому полю

}
