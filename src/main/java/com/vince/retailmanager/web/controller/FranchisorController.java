package com.vince.retailmanager.web.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.vince.retailmanager.exception.EntityNotFoundException;
import com.vince.retailmanager.model.entity.companies.Franchisee;
import com.vince.retailmanager.model.entity.companies.Franchisor;
import com.vince.retailmanager.model.entity.fees.FeeType;
import com.vince.retailmanager.model.entity.fees.MarketingFee;
import com.vince.retailmanager.model.entity.fees.PercentageFee;
import com.vince.retailmanager.model.entity.fees.Royalty;
import com.vince.retailmanager.model.entity.financials.IncomeStatement;
import com.vince.retailmanager.service.FinancialService;
import com.vince.retailmanager.service.FranchiseService;
import com.vince.retailmanager.service.TransactionService;
import com.vince.retailmanager.service.UserService;
import com.vince.retailmanager.utils.ValidatorUtils;
import com.vince.retailmanager.web.constants.ParamValues;
import com.vince.retailmanager.web.json.View;
import com.vince.retailmanager.web.utils.ModelUtils;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
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
@RequestMapping(value = {"/franchisors"})
public class FranchisorController {

  @Autowired
  public UserService userService;
  @Autowired
  public TransactionService transactionService;
  @Autowired
  public FinancialService financialService;
  @Autowired
  private FranchiseService franchiseService;
  @Autowired
  public ModelUtils modelUtils;
  @Autowired(required = false)
  public ValidatorUtils validatorUtils;

  @ModelAttribute
  public void populateModel(
      Model model,
      @PathVariable(value = "franchisorId", required = false) Integer franchisorId,
      @PathVariable(value = "franchiseeId", required = false) Integer franchiseeId,
      @PathVariable(value = "incomeStatementId", required = false) Integer incomeStatementId
  ) throws EntityNotFoundException {
    modelUtils.setModel(model);
    modelUtils.addFranchisor(franchisorId);
    modelUtils.addFranchisee(franchiseeId);
    modelUtils.addIncomeStatement(incomeStatementId);
  }


  @GetMapping("/{franchisorId}")
  @JsonView(View.Franchisor.class)
  public ResponseEntity<Franchisor> findFranchisor(Franchisor franchisor) {
    return new ResponseEntity<>(franchisor, HttpStatus.OK);
  }

  @PostMapping("/new")
  @JsonView(View.Franchisor.class)
  public ResponseEntity<Franchisor> createFranchisor(
      @Valid @RequestBody Franchisor franchisor,
      @AuthenticationPrincipal org.springframework.security.core.userdetails.User authenticatedUser
  ) {
    franchisor = franchiseService.saveCompany(franchisor);
    userService.addAccessToken(authenticatedUser.getUsername(), franchisor);
    return new ResponseEntity<>(franchisor, HttpStatus.CREATED);
  }

  @PutMapping("/{franchisorId}")
  @PreAuthorize("@modelUtils.isAuthorized(#franchisor)")
  @JsonView(View.Summary.class)
  public ResponseEntity<Franchisor> updateFranchisor(
      Franchisor franchisor,
      @RequestBody Franchisor updatedFranchisor
  ) {
    franchisor.setName(updatedFranchisor.getName());
    franchisor.setDescription(updatedFranchisor.getDescription());
    franchisor.setWebsite(updatedFranchisor.getWebsite());
    validatorUtils.validate(franchisor);
    franchiseService.saveCompany(franchisor);
    return new ResponseEntity<>(franchisor, HttpStatus.OK);
  }

  @PostMapping("/{franchisorId}/franchisees/new")
  @PreAuthorize("@modelUtils.isAuthorized(#franchisor)")
  @JsonView(View.Summary.class)
  public ResponseEntity<Franchisee> createFranchisee(
      Franchisor franchisor,
      @RequestBody Franchisee franchisee,
      @AuthenticationPrincipal org.springframework.security.core.userdetails.User authenticatedUser
  ) {
    franchisor.addFranchisee(franchisee);
    validatorUtils.validate(franchisee);
    franchisee = this.franchiseService.saveCompany(franchisee);
    userService.addAccessToken(authenticatedUser.getUsername(), franchisee);
    return new ResponseEntity<>(franchisee, HttpStatus.CREATED);
  }

  @GetMapping("/{franchisorId}/franchisees")
  @PreAuthorize("@modelUtils.isAuthorized(#franchisor)")
  @JsonView(View.Public.class)
  public ResponseEntity<Collection<Franchisee>> getFranchisees(Franchisor franchisor) {
    return new ResponseEntity<>(franchisor.getFranchisees(), HttpStatus.OK);
  }

  @DeleteMapping("/{franchisorId}")
  @PreAuthorize("@modelUtils.isAuthorized(#franchisor)")
  public ResponseEntity<Void> deleteFranchisor(Franchisor franchisor) throws Exception {
    franchiseService.disable(franchisor);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @PostMapping("/{franchisorId}/franchisees/{franchiseeId}/request-fees/financials/{incomeStatementId}")
  @PreAuthorize("@modelUtils.isAuthorized(#franchisor)")
  @JsonView(View.Summary.class)
  public ResponseEntity<Collection<PercentageFee>> requestMonthlyFranchiseFees(
      Franchisor franchisor,
      IncomeStatement incomeStatement
  ) {
    List<PercentageFee> fees = franchiseService
        .createMonthlyFranchiseFees(incomeStatement, Royalty::new, MarketingFee::new);
    return new ResponseEntity<>(fees, HttpStatus.OK);
  }

  @GetMapping("{franchisorId}/fees")
  @PreAuthorize("@modelUtils.isAuthorized(#franchisor)")
  @JsonView(View.Summary.class)
  public ResponseEntity<Collection<PercentageFee>> getFees(
      Franchisor franchisor,
      @RequestParam(name = "fee-type", required = false) final FeeType type,
      @RequestParam(required = false, name = ParamValues.FULLY_PAID) final Boolean isFullyPaid
  ) {
    Collection<PercentageFee> fees = franchiseService.getPercentageFees(franchisor, type);
    fees = isFullyPaid == null ? fees
        : filterPercentageFeesByFullyPaid(fees, isFullyPaid);
    return new ResponseEntity<>(fees, HttpStatus.OK);
  }

  private Collection<PercentageFee> filterPercentageFeesByFullyPaid(Collection<PercentageFee> fees,
      boolean isFullyPaid) {
    fees = fees.stream()
        .filter(fee -> fee.getInvoice().isFullyPaid() == isFullyPaid)
        .collect(Collectors.toSet());
    return fees;
  }

}
