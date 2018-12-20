package com.vince.retailmanager.service;

import com.vince.retailmanager.model.DistributionType;
import com.vince.retailmanager.model.entity.Company;
import com.vince.retailmanager.model.entity.Invoice;
import com.vince.retailmanager.model.entity.Payment;
import com.vince.retailmanager.repository.InvoiceRepository;
import com.vince.retailmanager.repository.PaymentRepository;
import com.vince.retailmanager.web.exception.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransactionServiceImpl implements TransactionService {

  private PaymentRepository paymentRepository;
  private InvoiceRepository invoiceRepository;

  @Autowired
  public TransactionServiceImpl(
      PaymentRepository paymentRepository,
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


  @Override
  @Transactional(readOnly = true)
  public Collection<Payment> getPayments(Company company, DistributionType type) {
    return getItemsByDistributionType(type, company,
        paymentRepository::findAllBySenderId,
        paymentRepository::findAllByRecipientId
    );
  }

  @Override
  @Transactional(readOnly = true)
  public Collection<Invoice> getInvoices(Company company, DistributionType type) {
    return getItemsByDistributionType(type, company,
        invoiceRepository::findAllBySenderId,
        invoiceRepository::findAllByRecipientId
    );
  }

  private <T> Collection<T> getItemsByDistributionType(
      DistributionType type, Company company,
      Function<Integer, Collection<T>> getItemsSent,
      Function<Integer, Collection<T>> getItemsReceived
  ) {
    switch (type) {
      case SENT:
        return getItemsSent.apply(company.getId());
      case RECEIVED:
        return getItemsReceived.apply(company.getId());
      default:
        return Collections.emptySet();
    }
  }


}
