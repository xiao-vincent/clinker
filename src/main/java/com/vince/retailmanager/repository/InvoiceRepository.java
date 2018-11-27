package com.vince.retailmanager.repository;

import com.vince.retailmanager.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceRepository extends JpaRepository<Invoice, Integer> {

}
