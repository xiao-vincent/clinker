package com.vince.retailmanager.repository;

import com.vince.retailmanager.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface InvoiceRepository extends JpaRepository<Invoice, Integer> {
	Collection<Invoice> findAllByRecipientId(int id);

	Collection<Invoice> findAllBySenderId(int id);

}
