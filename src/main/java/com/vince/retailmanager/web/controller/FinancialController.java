package com.vince.retailmanager.web.controller;

import com.vince.retailmanager.entity.Company;
import com.vince.retailmanager.entity.IncomeStatement;
import com.vince.retailmanager.entity.Validation;
import com.vince.retailmanager.service.FinancialService;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Validator;

@RestController
@RequestMapping("/financials/{companyId}")
public class FinancialController {
	@Autowired
	private FranchiseService franchiseService;
	@Autowired
	public UserService userService;
	@Autowired
	public PaymentService paymentService;
	@Autowired
	public FinancialService financialService;

	@Autowired
	public Validator validator;


	@ModelAttribute
	public void populateModel(
		 Model model,
		 @AuthenticationPrincipal org.springframework.security.core.userdetails.User authenticatedUser,
		 @PathVariable("companyId") Integer companyId
	) throws EntityNotFoundException {
		addCompany(model, companyId);
		ControllerUtils.addActiveUsername(model, authenticatedUser, companyId, userService);
	}

	private void addCompany(Model model, @PathVariable("companyId") Integer companyId) throws EntityNotFoundException {
		if (companyId != null) model.addAttribute("company", franchiseService.findCompanyById(companyId));
	}

	@PostMapping("/income-statements/new")
	public ResponseEntity<IncomeStatement> createIncomeStatement(Company company,
	                                                             @RequestBody @Validated
		                                                              IncomeStatement incomeStatement,
	                                                             @RequestParam(value = "year") int year,
	                                                             @RequestParam(value = "month") int month) {
		company.addIncomeStatement(incomeStatement);
//		incomeStatement.setDate(year, month);
		ControllerUtils.validate(validator, incomeStatement, Validation.Entity.class);
		incomeStatement = financialService.saveIncomeStatement(incomeStatement);
		return new ResponseEntity<>(incomeStatement, HttpStatus.OK);
	}

}
