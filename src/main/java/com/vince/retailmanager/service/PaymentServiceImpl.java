package com.vince.retailmanager.service;

import com.vince.retailmanager.entity.Invoice;
import com.vince.retailmanager.entity.Payment;
import com.vince.retailmanager.repository.InvoiceRepository;
import com.vince.retailmanager.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentServiceImpl implements PaymentService {

	private PaymentRepository paymentRepository;
	private InvoiceRepository invoiceRepository;

	@Autowired
	public PaymentServiceImpl(PaymentRepository paymentRepository,
	                          InvoiceRepository invoiceRepository) {
		this.paymentRepository = paymentRepository;
		this.invoiceRepository = invoiceRepository;
	}

	@Override
	@Transactional
	public void savePayment(Payment payment) {
		paymentRepository.save(payment);
	}

	@Override
	public void saveInvoice(Invoice invoice) {
		invoiceRepository.save(invoice);
	}

}
