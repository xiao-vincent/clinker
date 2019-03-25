package com.vince.retailmanager.web.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.vince.retailmanager.exception.EntityNotFoundException;
import com.vince.retailmanager.model.entity.companies.Company;
import com.vince.retailmanager.model.entity.financials.DateRange;
import com.vince.retailmanager.model.entity.financials.IncomeStatement;
import com.vince.retailmanager.model.entity.financials.IncomeStatementStatistics;
import com.vince.retailmanager.model.entity.financials.utils.IncomeStatementUtils;
import com.vince.retailmanager.service.FinancialService;
import com.vince.retailmanager.service.TransactionService;
import com.vince.retailmanager.service.UserService;
import com.vince.retailmanager.utils.ValidatorUtils;
import com.vince.retailmanager.web.constants.Date;
import com.vince.retailmanager.web.json.View;
import com.vince.retailmanager.web.json.View.Public;
import com.vince.retailmanager.web.json.View.Summary;
import com.vince.retailmanager.web.utils.ModelUtils;
import com.vince.retailmanager.web.validator.ValidationGroups;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
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
@RequestMapping("/financial/{companyId}")
public class FinancialController {

  @Autowired
  public UserService userService;
  @Autowired
  public TransactionService transactionService;
  @Autowired
  public FinancialService financialService;
  @Autowired
  public ModelUtils modelUtils;
  @Autowired
  public ValidatorUtils validatorUtils;

  @ModelAttribute
  public void populateModel(
      Model model,
      @PathVariable("companyId") Integer companyId,
      @PathVariable(value = "incomeStatementId", required = false) Integer incomeStatementId
  ) throws EntityNotFoundException {
    modelUtils.setModel(model);
    modelUtils.addCompany(companyId);
    modelUtils.addIncomeStatement(incomeStatementId);
  }

  @ModelAttribute("/income-statements")
  public void populateModelIncomeStatements(
      Model model,
      @RequestParam(required = false) @DateTimeFormat(pattern = Date.YEAR_MONTH_FORMAT) LocalDate startDate,
      @RequestParam(required = false) @DateTimeFormat(pattern = Date.YEAR_MONTH_FORMAT) LocalDate endDate) {
    modelUtils.addDateRange(model, startDate, endDate);
  }

  @GetMapping("/info")
  @JsonView(View.Financials.class)
  @PreAuthorize("@modelUtils.isAuthorized(#company)")
  public ResponseEntity<Company> getFinancials(Company company) {
    return new ResponseEntity<>(company, HttpStatus.OK);

  }

  @GetMapping("/income-statements/{incomeStatementId}")
  @PreAuthorize("@modelUtils.isAuthorizedInFranchise(#company)")
  @JsonView(View.Summary.class)
  public ResponseEntity<IncomeStatement> getIncomeStatement(Company company,
      IncomeStatement incomeStatement) {
    return new ResponseEntity<>(incomeStatement, HttpStatus.OK);
  }


  @PutMapping("/income-statements/{incomeStatementId}")
  @PreAuthorize("@modelUtils.isAuthorized(#company)")
  @JsonView(View.Summary.class)
  public ResponseEntity<IncomeStatement> updateIncomeStatement(
      Company company,
      IncomeStatement incomeStatement,
      @RequestBody @Validated IncomeStatement update
  ) {
    incomeStatement.setSales(update.getSales());
    incomeStatement.setCostOfGoodsSold(update.getCostOfGoodsSold());
    incomeStatement.setOperatingExpenses(update.getOperatingExpenses());
    incomeStatement.setGeneralAndAdminExpenses(update.getGeneralAndAdminExpenses());
    validatorUtils.validate(incomeStatement, ValidationGroups.Entity.class);
    financialService.saveIncomeStatement(incomeStatement);
    return new ResponseEntity<>(incomeStatement, HttpStatus.OK);
  }

  @PostMapping("/income-statements/new")
  @PreAuthorize("@modelUtils.isAuthorized(#company)")
  @JsonView(View.Summary.class)
  public ResponseEntity<IncomeStatement> createIncomeStatement(
      Company company,
      @RequestBody @Validated IncomeStatement incomeStatement,
      @RequestParam @DateTimeFormat(pattern = Date.YEAR_MONTH_FORMAT) LocalDate date) {
    company.addIncomeStatement(incomeStatement);
    incomeStatement.setDate(date);
    validatorUtils.validate(incomeStatement, ValidationGroups.Entity.class);
    incomeStatement = financialService.saveIncomeStatement(incomeStatement);
    return new ResponseEntity<>(incomeStatement, HttpStatus.OK);
  }

  @GetMapping("/income-statements")
  @PreAuthorize("@modelUtils.isAuthorizedInFranchise(#company)")
  @JsonView(Public.class)
  public ResponseEntity<Collection<IncomeStatement>> getIncomeStatements(
      Company company,
      DateRange dateRange
  ) {
    List<IncomeStatement> incomeStatements = IncomeStatementUtils
        .getSortedIncomeStatementsInDateRange(company, dateRange);
    return new ResponseEntity<>(incomeStatements, HttpStatus.OK);
  }

  @PostMapping("/income-statements/missing")
  @PreAuthorize("@modelUtils.isAuthorizedInFranchise(#company)")
  public ResponseEntity<Collection<LocalDate>> getMissingIncomeStatementDates(
      Company company,
      DateRange dateRange
  ) {
    Collection<LocalDate> missingDates = IncomeStatementUtils
        .getMissingIncomeStatementDates(company, dateRange);
    return new ResponseEntity<>(missingDates, HttpStatus.OK);
  }


  @GetMapping("/income-statements/statistics")
  @PreAuthorize("@modelUtils.isAuthorizedInFranchise(#company)")
  @JsonView(Summary.class)
  public ResponseEntity<IncomeStatementStatistics> getIncomeStatementStats(
      Company company,
      DateRange dateRange
  ) {
    IncomeStatementStatistics stats = IncomeStatementStatistics.create(company, dateRange);
    validatorUtils.validate(stats);
    return new ResponseEntity<>(stats, HttpStatus.OK);
  }
}

