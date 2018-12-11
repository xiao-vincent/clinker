package com.vince.retailmanager.service;

import com.vince.retailmanager.entity.Invoice;
import com.vince.retailmanager.entity.Payment;
import com.vince.retailmanager.web.exception.EntityNotFoundException;

public interface PaymentService {
	void savePayment(Payment payment);

//	void saveInvoice(Company sender, Company customer);

	void saveInvoice(Invoice invoice);

	Invoice findInvoiceById(int id) throws EntityNotFoundException;

	Payment findPaymentById(int id) throws EntityNotFoundException;
}
