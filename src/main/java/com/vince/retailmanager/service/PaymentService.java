package com.vince.retailmanager.service;

import com.vince.retailmanager.entity.Company;
import com.vince.retailmanager.entity.DistributionType;
import com.vince.retailmanager.entity.Invoice;
import com.vince.retailmanager.entity.Payment;
import com.vince.retailmanager.web.exception.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

public interface PaymentService {
	void savePayment(Payment payment);

//	void saveInvoice(Company sender, Company customer);

	void saveInvoice(Invoice invoice);

	Invoice findInvoiceById(int id) throws EntityNotFoundException;

	Payment findPaymentById(int id) throws EntityNotFoundException;

	@Transactional(readOnly = true)
	Collection<Payment> getPayments(Company company, DistributionType type);

	@Transactional(readOnly = true)
	Collection<Invoice> getInvoices(Company company, DistributionType type);
}
