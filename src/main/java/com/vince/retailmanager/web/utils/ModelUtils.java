package com.vince.retailmanager.web.utils;

import com.vince.retailmanager.exception.EntityNotFoundException;
import com.vince.retailmanager.model.entity.authorization.AccessToken;
import com.vince.retailmanager.model.entity.companies.Company;
import com.vince.retailmanager.model.entity.companies.Franchisee;
import com.vince.retailmanager.model.entity.companies.Franchisor;
import com.vince.retailmanager.model.entity.financials.DateRange;
import com.vince.retailmanager.service.FinancialService;
import com.vince.retailmanager.service.FranchiseService;
import com.vince.retailmanager.service.TransactionService;
import com.vince.retailmanager.service.UserService;
import java.time.LocalDate;
import java.util.Set;
import lombok.Setter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

@Component
public class ModelUtils {

  @Setter
  private Model model;

  private FranchiseService franchiseService;
  private FinancialService financialService;
  private UserService userService;
  private TransactionService transactionService;

  public ModelUtils(FranchiseService franchiseService,
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

  public boolean isAuthorized(Company... companies) {
    System.out.println("companies = " + companies);
    String username = SecurityContextHolder.getContext().getAuthentication().getName();
    if (username.isEmpty()) {
      return false;
    }
    AccessToken accessToken = userService.findAccessToken(username, Set.of(companies));
    System.out.println("accessToken = " + accessToken);
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
