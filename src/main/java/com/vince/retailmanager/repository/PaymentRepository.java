package com.vince.retailmanager.repository;

import com.vince.retailmanager.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {
	Collection<Payment> findAllByRecipientId(int id);

	Collection<Payment> findAllBySenderId(int id);
}
