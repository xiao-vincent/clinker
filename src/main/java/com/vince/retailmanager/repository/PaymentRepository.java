package com.vince.retailmanager.repository;

import com.vince.retailmanager.model.entity.transactions.Payment;
import java.util.Collection;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {

  Collection<Payment> findAllByRecipientId(int id);

  Collection<Payment> findAllBySenderId(int id);

  Optional<Payment> findByRefundedPaymentId(Integer id);
}
