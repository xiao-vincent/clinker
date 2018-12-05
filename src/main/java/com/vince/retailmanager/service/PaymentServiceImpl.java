package com.vince.retailmanager.service;

import com.vince.retailmanager.entity.Company;
import com.vince.retailmanager.entity.Invoice;
import com.vince.retailmanager.entity.Payment;
import com.vince.retailmanager.repository.InvoiceRepository;
import com.vince.retailmanager.repository.PaymentRepository;
import com.vince.retailmanager.web.exception.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

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
		Company sender = payment.getSender();
		BigDecimal newCashBalance = sender.getCashBalance().subtract(payment.getAmount());
		sender.setCashBalance(newCashBalance);
		paymentRepository.save(payment);
	}

	@Override
	public void saveInvoice(Invoice invoice) {
		invoiceRepository.save(invoice);
	}

	@Override
	@Transactional(readOnly = true)
	public Invoice findInvoiceById(int id) throws EntityNotFoundException {
		Invoice invoice = invoiceRepository.findById(id).orElse(null);
		if (invoice == null) {
			throw new EntityNotFoundException(Invoice.class, "id", String.valueOf(id));
		}

		return invoice;
	}

	@Override
	@Transactional(readOnly = true)
	public Payment findPaymentById(int id) throws EntityNotFoundException {
		Payment payment = paymentRepository.findById(id).orElse(null);
		if (payment == null) {
			throw new EntityNotFoundException(Payment.class, "id", String.valueOf(id));
		}
		return payment;
	}
}
