package com.vince.retailmanager.web.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.vince.retailmanager.entity.*;
import com.vince.retailmanager.service.FranchiseService;
import com.vince.retailmanager.service.PaymentService;
import com.vince.retailmanager.service.UserService;
import com.vince.retailmanager.web.controller.utils.ControllerUtils;
import com.vince.retailmanager.web.exception.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Validator;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/payments/{companyId}")
public class PaymentController {

	@Autowired
	private FranchiseService franchiseService;
	@Autowired
	public UserService userService;
	@Autowired
	public PaymentService paymentService;

	@Autowired
	public Validator validator;


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

	private void addCompany(Model model, @PathVariable("companyId") Integer companyId) throws EntityNotFoundException {
		if (companyId != null) model.addAttribute("company", franchiseService.findCompanyById(companyId));
	}

	private void addPayment(Model model, Integer paymentId) throws EntityNotFoundException {
		if (paymentId != null) model.addAttribute("payment", paymentService.findPaymentById(paymentId));
	}

	private void addInvoice(Model model, Integer invoiceId) throws EntityNotFoundException {
		if (invoiceId != null) model.addAttribute("invoice", paymentService.findInvoiceById(invoiceId));
	}


	@GetMapping("/received")
	@JsonView(View.Public.class)
	public ResponseEntity<Set<Payment>> getPaymentsReceived(Company company) {
		return new ResponseEntity<>(company.getPaymentsReceived(), HttpStatus.OK);
	}

	@GetMapping("/sent")
	@JsonView(View.Public.class)
	public ResponseEntity<Set<Payment>> getPaymentsSent(Company company) {
		return new ResponseEntity<>(company.getPaymentsSent(), HttpStatus.OK);
	}

	@GetMapping("/sent/{paymentId}")
	@JsonView(View.Summary.class)
	public ResponseEntity<Payment> getPaymentSent(Payment payment) {
		return new ResponseEntity<>(payment, HttpStatus.OK);
	}

	@GetMapping("/received/{paymentId}")
	@JsonView(View.Summary.class)
	public ResponseEntity<Payment> getPaymentReceived(Payment payment) {
		return new ResponseEntity<>(payment, HttpStatus.OK);
	}

	@GetMapping("/invoices/{distributionType}")
	@JsonView(View.Public.class)
	public ResponseEntity<Set<Invoice>> getInvoices(
		 Company company,
		 @PathVariable("distributionType") String type,
		 @MatrixVariable(required = false, name = "fully-paid") final Boolean isFullyPaid
	) throws EntityNotFoundException {
		Set<Invoice> invoices = getInvoicesByType(type, company);
		invoices = filterInvoicesByFullyPaid(invoices, isFullyPaid);
		return new ResponseEntity<>(invoices, HttpStatus.OK);
	}

	private Set<Invoice> filterInvoicesByFullyPaid(Set<Invoice> invoices, Boolean isFullyPaid) {
		if (isFullyPaid == null) return invoices;
		else
			return invoices.stream()
				 .filter(invoice -> invoice.isFullyPaid() == isFullyPaid)
				 .collect(Collectors.toSet());
	}

	private Set<Invoice> getInvoicesByType(String distributionTypeStr, Company company) throws EntityNotFoundException {
		if (!DistributionType.contains(distributionTypeStr)) {
			throw new EntityNotFoundException(DistributionType.class, "type", distributionTypeStr);
		}
		DistributionType type = DistributionType.valueOf(distributionTypeStr.toUpperCase());
		switch (type) {
			case SENT:
				return company.getInvoicesSent();
			case RECEIVED:
				return company.getInvoicesReceived();
		}
		return Collections.emptySet();
	}

	//preauth invoice.getCustomer == authentication.name
	@JsonView(View.Payment.class)
	@PostMapping("/invoices/{invoiceId}/pay")
	public ResponseEntity<Payment> payInvoice(@RequestBody Map<String, Object> body,
	                                          Invoice invoice,
	                                          Company company
	) {
		double paymentAmount = (double) ((Integer) body.get("amount"));
		Company recipient = invoice.getSender();

		Payment payment = Payment.builder()
			 .amount(paymentAmount)
			 .build();
		payment.addInvoice(invoice);
		company.addPaymentSent(payment);
		recipient.addPaymentReceived(payment);
		ControllerUtils.validate(validator, payment);
		paymentService.savePayment(payment);
		return new ResponseEntity<>(payment, HttpStatus.OK);
	}

	@JsonView(View.Payment.class)
	@PostMapping("/refunds/{paymentId}")
	public ResponseEntity<Payment> refundPayment(Payment paymentToRefund) {
		// check if payment has already been refunded
		Payment refund = Payment.builder()
			 .refundedPayment(paymentToRefund)
			 //can be simplified by method
//			 .sender(paymentToRefund.getRecipient())
//			 .recipient(paymentToRefund.getSender())
			 .amount(paymentToRefund.getAmount().doubleValue())
			 .build();
		refund.addInvoice(paymentToRefund.getInvoice());

		Company refundSender = paymentToRefund.getRecipient();
		Company refundRecipient = paymentToRefund.getSender();

		refundSender.addPaymentSent(refund);
		refundRecipient.addPaymentReceived(refund);

		ControllerUtils.validate(validator, refund);
		paymentService.savePayment(refund);

		return new ResponseEntity<>(refund, HttpStatus.OK);
	}


}
