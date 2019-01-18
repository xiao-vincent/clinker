package com.vince.retailmanager.web.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.vince.retailmanager.exception.EntityNotFoundException;
import com.vince.retailmanager.model.entity.companies.Company;
import com.vince.retailmanager.model.entity.transactions.DistributionType;
import com.vince.retailmanager.model.entity.transactions.Invoice;
import com.vince.retailmanager.model.entity.transactions.Payment;
import com.vince.retailmanager.service.FranchiseService;
import com.vince.retailmanager.service.TransactionService;
import com.vince.retailmanager.service.UserService;
import com.vince.retailmanager.utils.ValidatorUtils;
import com.vince.retailmanager.web.constants.ModelValue;
import com.vince.retailmanager.web.json.View;
import com.vince.retailmanager.web.utils.ModelUtils;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("transactions/{companyId}")
public class TransactionsController {

  @Autowired
  public UserService userService;
  @Autowired
  private FranchiseService franchiseService;
  @Autowired
  public TransactionService transactionService;
  @Autowired
  public ModelUtils modelUtils;
  @Autowired
  public ValidatorUtils validatorUtils;

  @ModelAttribute
  public void populateModel(
      Model model,
      @PathVariable("companyId") Integer companyId,
      @PathVariable(value = "invoiceId", required = false) Integer invoiceId,
      @PathVariable(value = "paymentId", required = false) Integer paymentId
  ) throws EntityNotFoundException {
    modelUtils.setModel(model);
    modelUtils.addCompany(companyId);
    modelUtils.addInvoice(invoiceId);
    modelUtils.addPayment(paymentId);
  }

  @GetMapping("/payments/{paymentId}")
  @PreAuthorize("@modelUtils.isAuthorized(#payment.getSender(),#payment.getRecipient() )")
  @JsonView(View.Summary.class)
  public ResponseEntity<Payment> getPayment(Payment payment) {
    return new ResponseEntity<>(payment, HttpStatus.OK);
  }

  @GetMapping("/payments")
  @PreAuthorize("@modelUtils.isAuthorized(#company)")
  @JsonView(View.Public.class)
  public ResponseEntity<Collection<Payment>> getPayments(Company company,
      @RequestParam(name = ModelValue.DISTRIBUTION_TYPE) DistributionType type) {
    Collection<Payment> payments = transactionService.getPayments(company, type);
    return new ResponseEntity<>(payments, HttpStatus.OK);
  }

  @GetMapping("/invoices")
  @PreAuthorize("@modelUtils.isAuthorized(#company)")
  @JsonView(View.Public.class)
  public ResponseEntity<Collection<Invoice>> getInvoices(
      Company company,
      @RequestParam(ModelValue.DISTRIBUTION_TYPE) DistributionType type,
      @RequestParam(required = false, name = ModelValue.FULLY_PAID) final Boolean isFullyPaid) {
    Collection<Invoice> invoices = transactionService.getInvoices(company, type);
    invoices = isFullyPaid == null ? invoices
        : filterInvoicesByFullyPaid(invoices, isFullyPaid);
    return new ResponseEntity<>(invoices, HttpStatus.OK);
  }

  @GetMapping("/invoices/{invoiceId}")
  @PreAuthorize("@modelUtils.isAuthorized(#invoice.getRecipient(), #invoice.getSender())")
  @JsonView(View.Invoice.class)
  public ResponseEntity<Invoice> getInvoice(Invoice invoice) {
    return new ResponseEntity<>(invoice, HttpStatus.OK);
  }

  @PostMapping("/invoices/{recipientId}")
  @PreAuthorize("@modelUtils.isAuthorized(#company)")
  @JsonView(View.Invoice.class)
  public ResponseEntity<Invoice> createInvoice(Company company,
      @RequestBody Invoice invoice,
      @PathVariable("recipientId") int recipientId
  ) throws EntityNotFoundException {
    invoice.setSender(company);
    invoice.setRecipient(franchiseService.findCompanyById(recipientId));
    validatorUtils.validate(invoice);
    transactionService.saveInvoice(invoice);
    return new ResponseEntity<>(invoice, HttpStatus.OK);
  }

  @PutMapping("/invoices/{invoiceId}")
  @PreAuthorize("@modelUtils.isAuthorized(#invoice.getSender())")
  @JsonView(View.Invoice.class)
  public ResponseEntity<Invoice> updateInvoice(Invoice invoice,
      @RequestBody Invoice updatedInvoice) {
    invoice.setDescription(updatedInvoice.getDescription());
    invoice.setDue(updatedInvoice.getDue());
    validatorUtils.validate(invoice);
    transactionService.saveInvoice(invoice);
    return new ResponseEntity<>(invoice, HttpStatus.OK);
  }


  @PostMapping("/invoices/{invoiceId}/void")
  @PreAuthorize("@modelUtils.isAuthorized(#invoice.getSender())")
  @JsonView(View.Invoice.class)
  public ResponseEntity<Invoice> voidInvoice(Invoice invoice) {
    invoice.setVoid(true);
    invoice.setDue(BigDecimal.ZERO);
    validatorUtils.validate(invoice);
    transactionService.saveInvoice(invoice);
    return new ResponseEntity<>(invoice, HttpStatus.OK);
  }

  @PostMapping("/invoices/{invoiceId}/pay")
  @PreAuthorize("@modelUtils.isAuthorized(#invoice.getRecipient())")
  @JsonView(View.Payment.class)
  public ResponseEntity<Payment> payInvoice(@RequestBody Map<String, Object> body,
      Invoice invoice) {
    double paymentAmount = (double) ((Integer) body.get("amount"));
    Payment payment = transactionService.payInvoice(invoice.getRecipient(), invoice, paymentAmount);
    return new ResponseEntity<>(payment, HttpStatus.OK);
  }

  @PostMapping("/refunds/{paymentId}")
  @PreAuthorize("@modelUtils.isAuthorized(#paymentToRefund.getRecipient())")
  @JsonView(View.Payment.class)
  public ResponseEntity<Payment> refundPayment(Payment paymentToRefund) {
    Payment refundPayment = transactionService.refundPayment(paymentToRefund);
    return new ResponseEntity<>(refundPayment, HttpStatus.OK);
  }

  private Collection<Invoice> filterInvoicesByFullyPaid(Collection<Invoice> invoices,
      boolean isFullyPaid) {
    return invoices.stream()
        .filter(invoice -> invoice.isFullyPaid() == isFullyPaid)
        .collect(Collectors.toSet());
  }
}
