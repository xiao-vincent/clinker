package com.vince.retailmanager.web.controller.Franchisor;

import com.vince.retailmanager.entity.Franchisee;
import com.vince.retailmanager.entity.Franchisor;
import com.vince.retailmanager.entity.Invoice;
import com.vince.retailmanager.entity.User;
import com.vince.retailmanager.service.FranchiseService;
import com.vince.retailmanager.service.PaymentService;
import com.vince.retailmanager.service.UserService;
import com.vince.retailmanager.web.controller.utils.ControllerUtils;
import com.vince.retailmanager.web.exception.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.Validator;
import java.util.Map;

@RestController
@RequestMapping(value = {"/franchisors"})
public class FranchisorController {
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
		 @PathVariable(value = "id", required = false) Integer id,
		 @PathVariable(value = "franchiseeId", required = false) Integer franchiseeId
	) throws EntityNotFoundException {
		if (id != null) {
			model.addAttribute("franchisor", franchiseService.findFranchisorById(id));
			ControllerUtils.addActiveUsername(model, authenticatedUser, id, userService);
		}

		if (franchiseeId != null) {
			model.addAttribute("franchisee", franchiseService.findFranchiseeById(franchiseeId));
		}
	}

	@InitBinder("franchisor")
	public void initFranchisorBinder(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@InitBinder("franchisee")
	public void initFranchiseeBinder(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}


	@GetMapping("/{id}")
	public ResponseEntity<Franchisor> findFranchisor(Franchisor franchisor) {
		return new ResponseEntity<>(franchisor, HttpStatus.OK);
	}

	@PostMapping("/new")
	public ResponseEntity<Franchisor> createCompany(@Valid @RequestBody Franchisor franchisor,
	                                                User user
	) {
		user.addAccessToken(franchisor);
		Franchisor savedFranchisor = franchiseService.saveFranchisor(franchisor);
		return new ResponseEntity<>(savedFranchisor, HttpStatus.CREATED);
	}

	@PutMapping("/{id}")
	@PreAuthorize("authentication.name == #activeUsername")
	public ResponseEntity<Franchisor> updateCompany(Franchisor franchisor,
	                                                @RequestBody Franchisor updatedFranchisor,
	                                                @ModelAttribute("activeUsername") String activeUsername
	) {
		franchisor.setName(updatedFranchisor.getName());
		franchisor.setDescription(updatedFranchisor.getDescription());
		franchisor.setWebsite(updatedFranchisor.getWebsite());
		franchiseService.saveCompany(franchisor);
		return new ResponseEntity<>(franchisor, HttpStatus.OK);
	}

	@PostMapping("/{id}/franchisees/new")
	@PreAuthorize("authentication.name == #activeUsername")
	public ResponseEntity<?> createFranchisee(Franchisor franchisor,
	                                          @ModelAttribute("activeUsername") String activeUsername
	) {
		Franchisee franchisee = Franchisee.builder().build();
		franchisor.addFranchisee(franchisee);
		ControllerUtils.validate(validator, franchisee);
		this.franchiseService.saveFranchisee(franchisee);
		return new ResponseEntity<>(franchisee, HttpStatus.CREATED);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("authentication.name == #activeUsername")
	public ResponseEntity<Void> deleteFranchisor(Franchisor franchisor) throws Exception {
		franchiseService.disableFranchisor(franchisor);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@PostMapping("/{id}/franchisees/{franchiseeId}/request-payment")
	public ResponseEntity<Invoice> requestPaymentFromFranchisee(
		 Franchisor franchisor,
		 Franchisee franchisee,
		 @RequestBody Map<String, Object> body
	) {
		double due = (double) body.get("due");
		Invoice invoice = Invoice.builder()
			 .seller(franchisor)
			 .customer(franchisee)
			 .balance(due)
			 .description("requesting royalty payment")
			 .build();
		ControllerUtils.validate(validator, invoice);
		paymentService.saveInvoice(invoice);
		return new ResponseEntity<>(invoice, HttpStatus.OK);
	}

}
