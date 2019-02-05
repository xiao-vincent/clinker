package com.vince.retailmanager.service;

import com.vince.retailmanager.exception.EntityNotFoundException;
import com.vince.retailmanager.model.entity.companies.Company;
import com.vince.retailmanager.model.entity.transactions.DistributionType;
import com.vince.retailmanager.model.entity.transactions.Invoice;
import com.vince.retailmanager.model.entity.transactions.Payment;
import java.util.Collection;

public interface TransactionService {

  Payment savePayment(Payment payment);


  void saveInvoice(Invoice invoice);

  Invoice findInvoiceById(int id) throws EntityNotFoundException;

  Payment findPaymentById(int id) throws EntityNotFoundException;

  Collection<Payment> getPayments(Company company, DistributionType type);

  Collection<Invoice> getInvoices(Company company, DistributionType type);

  Payment refundPayment(Payment paymentToRefund);

  Payment findRefund(Payment payment);

  Payment payInvoice(Company sender, Invoice invoice, double paymentAmount);
}
