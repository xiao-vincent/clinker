package com.vince.retailmanager.web.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.vince.retailmanager.entity.Company;
import com.vince.retailmanager.entity.Invoice;
import com.vince.retailmanager.entity.Payment;
import com.vince.retailmanager.entity.View;
import com.vince.retailmanager.service.FranchiseService;
import com.vince.retailmanager.service.PaymentService;
import com.vince.retailmanager.service.UserService;
import com.vince.retailmanager.web.exception.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Validator;
import java.util.Map;
import java.util.Set;

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
		 @PathVariable(value = "invoiceId", required = false) Integer invoiceId
//		 @PathVariable(value = "franchiseeId") Integer franchiseeId
	) throws EntityNotFoundException {
		if (companyId != null) {
			model.addAttribute("company", franchiseService.findCompanyById(companyId));
			ControllerUtils.addActiveUsername(model, authenticatedUser, companyId, userService);
		}
		if (invoiceId != null) {
			model.addAttribute("invoice", paymentService.findInvoiceById(invoiceId));
		}
	}

	@GetMapping("/invoices/received")
	@JsonView(View.Public.class)
	public ResponseEntity<Set<Invoice>> getReceivedInvoices(Company company) {
		return new ResponseEntity<>(company.getInvoicesReceived(), HttpStatus.OK);
	}

	@GetMapping("/invoices/sent")
	@JsonView(View.Public.class)
	public ResponseEntity<Set<Invoice>> getSentInvoices(Company company) {
		return new ResponseEntity<>(company.getInvoicesSent(), HttpStatus.OK);
	}


	//preauth invoice.getCustomer == authentication.name
	@JsonView(View.Payment.class)
	@PostMapping("/invoices/{invoiceId}/pay")
	public ResponseEntity<Payment> payInvoice(@RequestBody Map<String, Object> body,
	                                          Invoice invoice,
	                                          Company company
	) {
		double paymentAmount = (double) ((Integer) body.get("amount"));

		Payment payment = Payment.builder()
			 .sender(company)
			 .recipient(invoice.getSeller())
			 .amount(paymentAmount)
			 .build();
		payment.addInvoice(invoice);
		ControllerUtils.validate(validator, payment);
		paymentService.savePayment(payment);
		return new ResponseEntity<>(payment, HttpStatus.OK);
	}

}
