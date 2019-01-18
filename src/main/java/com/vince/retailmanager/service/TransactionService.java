package com.vince.retailmanager.service;

import com.vince.retailmanager.model.entity.companies.Company;
import com.vince.retailmanager.model.entity.transactions.DistributionType;
import com.vince.retailmanager.model.entity.transactions.Invoice;
import com.vince.retailmanager.model.entity.transactions.Payment;
import com.vince.retailmanager.exception.EntityNotFoundException;
import java.util.Collection;
import org.springframework.transaction.annotation.Transactional;

public interface TransactionService {

  Payment savePayment(Payment payment);

//	void saveInvoice(Company sender, Company customer);

  void saveInvoice(Invoice invoice);

  Invoice findInvoiceById(int id) throws EntityNotFoundException;

  Payment findPaymentById(int id) throws EntityNotFoundException;

  @Transactional(readOnly = true)
  Collection<Payment> getPayments(Company company, DistributionType type);

  @Transactional(readOnly = true)
  Collection<Invoice> getInvoices(Company company, DistributionType type);

  Payment refundPayment(Payment paymentToRefund);

  Payment findRefund(Payment payment);

  Payment payInvoice(Company sender, Invoice invoice, double paymentAmount);
}
