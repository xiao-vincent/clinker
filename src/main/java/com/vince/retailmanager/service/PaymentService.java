package com.vince.retailmanager.service;

import com.vince.retailmanager.entity.Invoice;
import com.vince.retailmanager.entity.Payment;

public interface PaymentService {
	void savePayment(Payment payment);

//	void saveInvoice(Company seller, Company customer);

	void saveInvoice(Invoice invoice);
}
