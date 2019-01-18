package com.vince.retailmanager.service;

import com.vince.retailmanager.exception.InvalidOperationException;
import com.vince.retailmanager.model.entity.companies.Company;
import com.vince.retailmanager.model.entity.transactions.DistributionType;
import com.vince.retailmanager.model.entity.transactions.Invoice;
import com.vince.retailmanager.model.entity.transactions.Payment;
import com.vince.retailmanager.repository.InvoiceRepository;
import com.vince.retailmanager.repository.PaymentRepository;
import com.vince.retailmanager.utils.ValidatorUtils;
import com.vince.retailmanager.exception.EntityNotFoundException;
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
  private ValidatorUtils validatorUtils;

  @Autowired
  public TransactionServiceImpl(
      PaymentRepository paymentRepository,
      InvoiceRepository invoiceRepository,
      ValidatorUtils validatorUtils) {
    this.paymentRepository = paymentRepository;
    this.invoiceRepository = invoiceRepository;
    this.validatorUtils = validatorUtils;
  }

  @Override
  @Transactional
  public Payment savePayment(Payment payment) {
    Company sender = payment.getSender();
    BigDecimal newCashBalance = sender.getCashBalance().subtract(payment.getAmount());
    sender.setCashBalance(newCashBalance);

    return paymentRepository.save(payment);
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

  @Override
  public Payment refundPayment(Payment paymentToRefund) {
    Company refundSender = paymentToRefund.getRecipient();
    Company refundRecipient = paymentToRefund.getSender();
    Payment refundPayment = Payment.builder()
        .refundedPayment(paymentToRefund)
        .sender(refundSender)
        .recipient(refundRecipient)
        .amount(paymentToRefund.getAmount().doubleValue())
        .build();
    refundPayment.addInvoice(paymentToRefund.getInvoice());
    if (this.findRefund(paymentToRefund) != null) {
      throw new InvalidOperationException("Payment already refunded");
    }
    validatorUtils.validate(refundPayment);
    this.savePayment(refundPayment);
    return refundPayment;
  }

  @Override
  public Payment findRefund(Payment payment) {
    return paymentRepository.findByRefundedPaymentId(payment.getId()).orElse(null);
  }

  @Override
  public Payment payInvoice(Company sender, Invoice invoice, double paymentAmount) {
    Company recipient = invoice.getSender();
    Payment payment = Payment.builder()
        .amount(paymentAmount)
        .sender(sender)
        .recipient(recipient)
        .build();
    payment.addInvoice(invoice);
    validatorUtils.validate(payment);
    return this.savePayment(payment);
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
