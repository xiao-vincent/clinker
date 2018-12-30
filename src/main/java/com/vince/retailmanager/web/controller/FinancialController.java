package com.vince.retailmanager.web.controller;

import com.vince.retailmanager.model.DateRange;
import com.vince.retailmanager.model.IncomeStatementStatistics;
import com.vince.retailmanager.model.Validation;
import com.vince.retailmanager.model.constants.Date;
import com.vince.retailmanager.model.entity.Company;
import com.vince.retailmanager.model.entity.Franchisee;
import com.vince.retailmanager.model.entity.IncomeStatement;
import com.vince.retailmanager.model.utils.IncomeStatementUtils;
import com.vince.retailmanager.service.FinancialService;
import com.vince.retailmanager.service.FranchiseService;
import com.vince.retailmanager.service.TransactionService;
import com.vince.retailmanager.service.UserService;
import com.vince.retailmanager.web.controller.utils.ControllerUtils;
import com.vince.retailmanager.web.exception.EntityNotFoundException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
@RequestMapping("/financials/{companyId}")
public class FinancialController {

  @Autowired
  public UserService userService;
  @Autowired
  public TransactionService transactionService;
  @Autowired
  public FinancialService financialService;
  @Autowired
  public ControllerUtils controllerUtils;
  @Autowired
  private FranchiseService franchiseService;

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

  @ModelAttribute("/income-statements")
  public void populateModel_IncomeStatements(
      Model model,
      @RequestParam @DateTimeFormat(pattern = Date.DATE_FORMAT) LocalDate startDate,
      @RequestParam @DateTimeFormat(pattern = Date.DATE_FORMAT) LocalDate endDate
  ) {
    DateRange dateRange = new DateRange(startDate, endDate);
    model.addAttribute("dateRange", dateRange);

  }

  private void addCompany(Model model,
      Integer companyId) throws EntityNotFoundException {
    if (companyId != null) {
      model.addAttribute("company", franchiseService.findCompanyById(companyId));
    }
  }

  private void addIncomeStatement(Model model,
      Integer incomeStatementId) throws EntityNotFoundException {
    if (incomeStatementId != null) {
      model.addAttribute("incomeStatement",
          financialService.findIncomeStatementById(incomeStatementId));
    }
  }

  @GetMapping("/income-statements/{incomeStatementId}")
  public ResponseEntity<IncomeStatement> findIncomeStatement(Company company,
      IncomeStatement incomeStatement) {
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
    controllerUtils.validate(incomeStatement, Validation.Entity.class);
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
    controllerUtils.validate(incomeStatement, Validation.Entity.class);
    incomeStatement = financialService.saveIncomeStatement(incomeStatement);

    if (incomeStatement.getCompany() instanceof Franchisee) {
      franchiseService.createMonthlyFranchiseFees(incomeStatement);
    }

    return new ResponseEntity<>(incomeStatement, HttpStatus.OK);
  }

  @GetMapping("/income-statements")
  public ResponseEntity<Collection<IncomeStatement>> getIncomeStatements(
      Company company,
      DateRange dateRange
  ) {
    List<IncomeStatement> incomeStatements = IncomeStatementUtils
        .getSortedIncomeStatementsInDateRange(company, dateRange);
    return new ResponseEntity<>(incomeStatements, HttpStatus.OK);
  }

  @PostMapping("/income-statements/missing")
  public ResponseEntity<Collection<YearMonth>> getMissingIncomeStatementDates(
      Company company,
      DateRange dateRange
  ) {
    Set<YearMonth> yearMonths = IncomeStatementUtils
        .getMissingIncomeStatementDates(company, dateRange);
    return new ResponseEntity<>(yearMonths, HttpStatus.OK);
  }


  @GetMapping("/income-statements/stats")
  public ResponseEntity<IncomeStatementStatistics> getIncomeStatementStats(
      Company company,
      DateRange dateRange
  ) {
    IncomeStatementStatistics stats = IncomeStatementStatistics.create(company, dateRange);
    controllerUtils.validate(stats);
    return new ResponseEntity<>(stats, HttpStatus.OK);
  }


}
