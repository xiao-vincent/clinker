package com.vince.retailmanager.repository;

import com.vince.retailmanager.model.entity.Payment;
import java.util.Collection;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {

  Collection<Payment> findAllByRecipientId(int id);

  Collection<Payment> findAllBySenderId(int id);
}
