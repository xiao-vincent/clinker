package com.vince.retailmanager.web.controller;

import com.vince.retailmanager.entity.Company;
import com.vince.retailmanager.entity.Franchisee;
import com.vince.retailmanager.entity.IncomeStatement;
import com.vince.retailmanager.entity.Validation;
import com.vince.retailmanager.entity.constants.Date;
import com.vince.retailmanager.service.FinancialService;
import com.vince.retailmanager.service.FranchiseService;
import com.vince.retailmanager.service.PaymentService;
import com.vince.retailmanager.service.UserService;
import com.vince.retailmanager.utils.DateUtils;
import com.vince.retailmanager.web.controller.utils.ControllerUtils;
import com.vince.retailmanager.web.exception.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Validator;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

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
			 @PathVariable("companyId") Integer companyId,
			 @PathVariable(value = "incomeStatementId", required = false) Integer incomeStatementId
													 ) throws EntityNotFoundException {
		addCompany(model, companyId);
		ControllerUtils.addActiveUsername(model, authenticatedUser, companyId, userService);

		addIncomeStatement(model, incomeStatementId);


	}

	private void addCompany(Model model, Integer companyId) throws EntityNotFoundException {
		if (companyId != null) model.addAttribute("company", franchiseService.findCompanyById(companyId));
	}


	private void addIncomeStatement(Model model, Integer incomeStatementId) throws EntityNotFoundException {
		if (incomeStatementId != null)
			model.addAttribute("incomeStatement", financialService.findIncomeStatementById(incomeStatementId));
	}

	@GetMapping("/income-statements/{incomeStatementId}")
	public ResponseEntity<IncomeStatement> findIncomeStatement(Company company, IncomeStatement incomeStatement) {
		return new ResponseEntity<>(incomeStatement, HttpStatus.OK);
	}

	@PutMapping("/income-statements/{incomeStatementId}")
	public ResponseEntity<IncomeStatement> updateIncomeStatement(
			 IncomeStatement incomeStatement,
			 @RequestBody @Validated IncomeStatement update,
			 @ModelAttribute("activeUsername") String activeUsername,
			 @RequestParam @DateTimeFormat(pattern = Date.DATE_FORMAT) LocalDate date
																															) {
		incomeStatement.setSales(update.getSales());
		incomeStatement.setCostOfGoodsSold(update.getCostOfGoodsSold());
		incomeStatement.setOperatingExpenses(update.getSales());
		incomeStatement.setGeneralAndAdminExpenses(update.getSales());
		incomeStatement.setDate(date);
		ControllerUtils.validate(validator, incomeStatement, Validation.Entity.class);
		financialService.saveIncomeStatement(incomeStatement);
		return new ResponseEntity<>(incomeStatement, HttpStatus.OK);
	}

	@PostMapping("/income-statements/new")
	public ResponseEntity<IncomeStatement> createIncomeStatement(
			 Company company,
			 @RequestBody @Validated IncomeStatement incomeStatement,
			 @RequestParam @DateTimeFormat(pattern = Date.DATE_FORMAT) LocalDate date) {
		company.addIncomeStatement(incomeStatement);
		incomeStatement.setDate(date);
		ControllerUtils.validate(validator, incomeStatement, Validation.Entity.class);
		incomeStatement = financialService.saveIncomeStatement(incomeStatement);

		if (incomeStatement.getCompany() instanceof Franchisee) {
			franchiseService.createMonthlyFranchiseFees(incomeStatement);
		}

		return new ResponseEntity<>(incomeStatement, HttpStatus.OK);
	}

	@GetMapping("/income-statements")
	public ResponseEntity<Collection<IncomeStatement>> getIncomeStatements(
			 Company company,
			 @RequestParam @DateTimeFormat(pattern = Date.DATE_FORMAT) LocalDate startDate,
			 @RequestParam @DateTimeFormat(pattern = Date.DATE_FORMAT) LocalDate endDate
																																				) {
		Set<IncomeStatement> incomeStatements = company.getIncomeStatements()
				 .stream()
				 .filter(incomeStatement -> DateUtils.checkBetween(incomeStatement.getDate(), startDate, endDate))
				 .collect(Collectors.toSet());
		return new ResponseEntity<>(incomeStatements, HttpStatus.OK);
	}


}
