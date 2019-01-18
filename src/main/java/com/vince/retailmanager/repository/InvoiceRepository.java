package com.vince.retailmanager.repository;

import com.vince.retailmanager.model.entity.transactions.Invoice;
import java.util.Collection;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceRepository extends JpaRepository<Invoice, Integer> {

  Collection<Invoice> findAllByRecipientId(int id);

  Collection<Invoice> findAllBySenderId(int id);

}
