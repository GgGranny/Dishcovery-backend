package com.dishcovery.backend.repo;


import com.dishcovery.backend.model.EsewaPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepo  extends JpaRepository<EsewaPayment, Integer> {

    @Query("Select payment FROM EsewaPayment payment WHERE payment.transaction_uuid = ?1 ")
    Optional<EsewaPayment> findByTransaction_Uuid(String transactionUuid);
}
