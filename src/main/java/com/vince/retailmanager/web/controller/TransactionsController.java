package com.vince.retailmanager.web.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.vince.retailmanager.model.DistributionType;
import com.vince.retailmanager.model.View;
import com.vince.retailmanager.model.entity.Company;
import com.vince.retailmanager.model.entity.Invoice;
import com.vince.retailmanager.model.entity.Payment;
import com.vince.retailmanager.service.FranchiseService;
import com.vince.retailmanager.service.TransactionService;
import com.vince.retailmanager.service.UserService;
import com.vince.retailmanager.utils.ValidatorUtils;
import com.vince.retailmanager.web.controller.utils.ControllerUtils;
import com.vince.retailmanager.web.exception.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;
import javax.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
  public Validator validator;
  @Autowired
  public ControllerUtils controllerUtils;
  @Autowired
  public ValidatorUtils validatorUtils;

  @ModelAttribute
  public void populateModel(
      Model model,
      @AuthenticationPrincipal org.springframework.security.core.userdetails.User authenticatedUser,
      @PathVariable("companyId") Integer companyId,
      @PathVariable(value = "invoiceId", required = false) Integer invoiceId,
      @PathVariable(value = "paymentId", required = false) Integer paymentId
  ) throws EntityNotFoundException {

    addCompany(model, companyId);
    ControllerUtils.addActiveUsername(model, authenticatedUser, companyId, userService);
    addInvoice(model, invoiceId);
    addPayment(model, paymentId);
  }

  private void addCompany(Model model,
      @PathVariable("companyId") Integer companyId) throws EntityNotFoundException {
    if (companyId != null) {
      model.addAttribute("company", franchiseService.findCompanyById(companyId));
    }
  }

  private void addPayment(Model model,
      Integer paymentId) throws EntityNotFoundException {
    if (paymentId != null) {
      model.addAttribute("payment", transactionService.findPaymentById(paymentId));
    }
  }

  private void addInvoice(Model model,
      Integer invoiceId) throws EntityNotFoundException {
    if (invoiceId != null) {
      model.addAttribute("invoice", transactionService.findInvoiceById(invoiceId));
    }
  }

  @GetMapping("/payments/{paymentId}")
  @JsonView(View.Summary.class)
  public ResponseEntity<Payment> getPayment(Payment payment) {
    return new ResponseEntity<>(payment, HttpStatus.OK);
  }

  @GetMapping("/payments")
  @JsonView(View.Public.class)
  public ResponseEntity<Collection<Payment>> getPayments(Company company,
      @RequestParam(name = "type") DistributionType type) {
    Collection<Payment> payments = transactionService.getPayments(company, type);
    return new ResponseEntity<>(payments, HttpStatus.OK);
  }


  @GetMapping("/invoices")
  @JsonView(View.Public.class)
  public ResponseEntity<Collection<Invoice>> getInvoices(
      Company company,
      @RequestParam("type") DistributionType type,
      @RequestParam(required = false, name = "fully-paid") final Boolean isFullyPaid) {
    Collection<Invoice> invoices = transactionService.getInvoices(company, type);
    invoices = isFullyPaid == null ? invoices : filterInvoicesByFullyPaid(invoices, isFullyPaid);
    return new ResponseEntity<>(invoices, HttpStatus.OK);
  }


  @GetMapping("/invoices/{invoiceId}")
  @JsonView(View.Invoice.class)
  public ResponseEntity<Invoice> getInvoice(Invoice invoice) {
    return new ResponseEntity<>(invoice, HttpStatus.OK);
  }

  @PostMapping("/invoices/{recipientId}")
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
  @JsonView(View.Invoice.class)
  public ResponseEntity<Invoice> voidInvoice(Invoice invoice) {
    invoice.setVoid(true);
    invoice.setDue(BigDecimal.ZERO);
    validatorUtils.validate(invoice);
    transactionService.saveInvoice(invoice);
    return new ResponseEntity<>(invoice, HttpStatus.OK);
  }

  @PostMapping("/invoices/{invoiceId}/pay")
  @JsonView(View.Payment.class)
  public ResponseEntity<Payment> payInvoice(@RequestBody Map<String, Object> body,
      Invoice invoice,
      Company company) {
    double paymentAmount = (double) ((Integer) body.get("amount"));
    Payment payment = transactionService.payInvoice(company, invoice, paymentAmount);
    return new ResponseEntity<>(payment, HttpStatus.OK);
  }


  @PostMapping("/refunds/{paymentId}")
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
