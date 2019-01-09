package com.vince.retailmanager.web.controller.utils;

import com.vince.retailmanager.model.DateRange;
import com.vince.retailmanager.model.entity.AccessToken;
import com.vince.retailmanager.model.entity.Company;
import com.vince.retailmanager.model.entity.Franchisee;
import com.vince.retailmanager.model.entity.Franchisor;
import com.vince.retailmanager.service.FinancialService;
import com.vince.retailmanager.service.FranchiseService;
import com.vince.retailmanager.service.TransactionService;
import com.vince.retailmanager.service.UserService;
import com.vince.retailmanager.web.exception.EntityNotFoundException;
import java.time.LocalDate;
import java.util.Set;
import lombok.Setter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

@Component
public class ControllerUtils {

  @Setter
  private Model model;

  private FranchiseService franchiseService;
  private FinancialService financialService;
  private UserService userService;
  private TransactionService transactionService;

  public ControllerUtils(FranchiseService franchiseService,
      FinancialService financialService, UserService userService,
      TransactionService transactionService) {
    this.franchiseService = franchiseService;
    this.financialService = financialService;
    this.userService = userService;
    this.transactionService = transactionService;
  }


  public void addCompany(Integer companyId) throws EntityNotFoundException {
    if (companyId != null) {
      model.addAttribute("company", franchiseService.findCompanyById(companyId));
    }
  }

  public void addDateRange(Model model,
      LocalDate startDate,
      LocalDate endDate) {
    DateRange dateRange = new DateRange(startDate, endDate);
    model.addAttribute("dateRange", dateRange);
  }

  public void addPayment(Integer paymentId) throws EntityNotFoundException {
    if (paymentId != null) {
      model.addAttribute("payment", transactionService.findPaymentById(paymentId));
    }
  }

  public void addInvoice(Integer invoiceId) throws EntityNotFoundException {
    if (invoiceId != null) {
      model.addAttribute("invoice", transactionService.findInvoiceById(invoiceId));
    }
  }

//  public void addAuthenticatedUsername(User authenticatedUser) {
//    if (authenticatedUser == null) {
//      return;
//    }
//    this.model.addAttribute(ModelValue.AUTH_USERNAME, authenticatedUser.getUsername());
//  }

  public boolean isAuthorized(Company... companies) {
    String username = SecurityContextHolder.getContext().getAuthentication().getName();
    AccessToken accessToken = userService.findAccessToken(username, Set.of(companies));
    return accessToken != null;
  }

  //if company is a franchisee, authorizes its franchisor
  //or just authorizes the company
  public boolean isAuthorizedInFranchise(Company company) {
    try {
      Franchisee franchisee = (Franchisee) company;
      Franchisor franchisor = franchisee.getFranchisor();
      return this.isAuthorized(franchisor, franchisee);
    } catch (ClassCastException e) {
      return this.isAuthorized(company);
    }

  }


  public void addFranchisee(Integer franchiseeId)
      throws EntityNotFoundException {
    if (franchiseeId != null) {
      model.addAttribute("franchisee", franchiseService.findFranchiseeById(franchiseeId));
    }
  }

  public void addFranchisor(Integer franchisorId)
      throws EntityNotFoundException {
    if (franchisorId != null) {
      model.addAttribute("franchisor", franchiseService.findFranchisorById(franchisorId));
    }
  }

  public void addIncomeStatement(Integer incomeStatementId)
      throws EntityNotFoundException {
    if (incomeStatementId != null) {
      model.addAttribute("incomeStatement",
          financialService.findIncomeStatementById(incomeStatementId));
    }
  }


}
