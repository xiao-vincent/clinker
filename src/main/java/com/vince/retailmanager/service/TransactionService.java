package com.vince.retailmanager.service;

import com.vince.retailmanager.exception.EntityNotFoundException;
import com.vince.retailmanager.model.entity.companies.Company;
import com.vince.retailmanager.model.entity.transactions.DistributionType;
import com.vince.retailmanager.model.entity.transactions.Invoice;
import com.vince.retailmanager.model.entity.transactions.Payment;
import java.util.Collection;
import javax.validation.constraints.NotNull;
import org.springframework.lang.Nullable;

public interface TransactionService {

  /**
   * Saves the payment entity.
   */
  @NotNull
  Payment savePayment(Payment payment);

  /**
   * Saves the invoice entity
   */
  @NotNull
  void saveInvoice(Invoice invoice);

  /**
   * Finds an invoice by ID.
   *
   * @param id the id of the invoice
   * @throws EntityNotFoundException if invoice is not found
   */
  @Nullable
  Invoice findInvoiceById(int id) throws EntityNotFoundException;

  /**
   * Finds a payment by ID.
   *
   * @param id the id of the payment
   * @throws EntityNotFoundException if payment is not found
   */
  @Nullable
  Payment findPaymentById(int id) throws EntityNotFoundException;

  /**
   * Gets a collection of payments
   *
   * @param type payment type (sent or received)
   * @return a collecion of payments
   */
  Collection<Payment> getPayments(@NotNull Company company, DistributionType type);

  /**
   * Gets a collection of invoices
   *
   * @param type invoice type (sent or received)
   * @return a collecion of invoices
   */
  Collection<Invoice> getInvoices(@NotNull Company company, DistributionType type);

  /**
   * Refunds a payment
   *
   * @param paymentToRefund the payment to be refunded
   * @return the refund payment
   */
  Payment refundPayment(@NotNull Payment paymentToRefund);

  /**
   * Finds a refund associated with the payment
   *
   * @param payment the payment that was refunded
   * @return the refund payment
   */
  Payment findRefund(Payment payment);

  /**
   * Pay an invoice
   *
   * @param sender the company paying the invoice
   * @param invoice the invoice to pay off
   * @return the payment paid towards the invoice balance
   */
  Payment payInvoice(@NotNull Company sender, @NotNull Invoice invoice, double paymentAmount);
}
