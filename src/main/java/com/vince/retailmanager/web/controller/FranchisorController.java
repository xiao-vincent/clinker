package com.vince.retailmanager.web.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.vince.retailmanager.model.View;
import com.vince.retailmanager.model.entity.Franchisee;
import com.vince.retailmanager.model.entity.Franchisor;
import com.vince.retailmanager.model.entity.IncomeStatement;
import com.vince.retailmanager.model.entity.Invoice;
import com.vince.retailmanager.model.entity.PercentageFee;
import com.vince.retailmanager.model.entity.PercentageFee.FeeType;
import com.vince.retailmanager.service.FinancialService;
import com.vince.retailmanager.service.FranchiseService;
import com.vince.retailmanager.service.TransactionService;
import com.vince.retailmanager.service.UserService;
import com.vince.retailmanager.utils.ValidatorUtils;
import com.vince.retailmanager.web.controller.utils.ControllerUtils;
import com.vince.retailmanager.web.exception.EntityNotFoundException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
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
  public ControllerUtils controllerUtils;
  @Autowired
  public ValidatorUtils validatorUtils;

  @ModelAttribute
  public void populateModel(
      Model model,
      @AuthenticationPrincipal org.springframework.security.core.userdetails.User authenticatedUser,
      @PathVariable(value = "franchisorId", required = false) Integer franchisorId,
      @PathVariable(value = "franchiseeId", required = false) Integer franchiseeId,
      @PathVariable(value = "incomeStatementId", required = false) Integer incomeStatementId
  ) throws EntityNotFoundException {
    if (franchisorId != null) {
      ControllerUtils.addActiveUsername(model, authenticatedUser, franchisorId, userService);
      controllerUtils.addUsername(model, authenticatedUser, franchisorId, userService);
    }
    controllerUtils.addFranchisorToModel(model, franchisorId);
    controllerUtils.addFranchiseeToModel(model, franchiseeId);
    controllerUtils.addIncomeStatementToModel(model, incomeStatementId);
  }

  @GetMapping("/{franchisorId}")
  @JsonView(View.Franchisor.class)
  public ResponseEntity<Franchisor> findFranchisor(Franchisor franchisor) {
    return new ResponseEntity<>(franchisor, HttpStatus.OK);
  }

  @PostMapping("/new")
  @JsonView(View.Franchisor.class)
  public ResponseEntity<Franchisor> createFranchisor(
      @Valid @RequestBody Franchisor franchisor
  ) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String currentPrincipalName = authentication.getName();
    franchisor = franchiseService.saveFranchisor(franchisor);
    userService.addAccessToken(currentPrincipalName, franchisor);
    return new ResponseEntity<>(franchisor, HttpStatus.CREATED);
  }

  @PutMapping("/{franchisorId}")
  @PreAuthorize("authentication.name == #activeUsername")
  @JsonView(View.Summary.class)
  public ResponseEntity<Franchisor> updateCompany(
      Franchisor franchisor,
      @RequestBody Franchisor updatedFranchisor,
      @ModelAttribute("activeUsername") String activeUsername
  ) {
    franchisor.setName(updatedFranchisor.getName());
    franchisor.setDescription(updatedFranchisor.getDescription());
    franchisor.setWebsite(updatedFranchisor.getWebsite());

    franchiseService.saveCompany(franchisor);
    validatorUtils.validate(franchisor);
    return new ResponseEntity<>(franchisor, HttpStatus.OK);
  }

  @PostMapping("/{franchisorId}/franchisees/new")
  @PreAuthorize("@controllerUtils.hasAccessTo(#franchisor)")
  @JsonView(View.Summary.class)
  public ResponseEntity<Franchisee> createFranchisee(
      Franchisor franchisor,
      @RequestBody Franchisee franchisee
  ) {
    franchisor.addFranchisee(franchisee);
    validatorUtils.validate(franchisee);
    this.franchiseService.saveFranchisee(franchisee);
    return new ResponseEntity<>(franchisee, HttpStatus.CREATED);
  }

  @GetMapping("/{franchisorId}/franchisees")
  @JsonView(View.Public.class)
  public ResponseEntity<Collection<Franchisee>> getFranchisees(Franchisor franchisor) {
    return new ResponseEntity<>(franchisor.getFranchisees(), HttpStatus.OK);
  }

  @DeleteMapping("/{franchisorId}")
  @PreAuthorize("authentication.name == #activeUsername")
  public ResponseEntity<Void> deleteFranchisor(Franchisor franchisor) throws Exception {
    franchiseService.disableFranchisor(franchisor);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @PostMapping("/{franchisorId}/franchisees/{franchiseeId}/request-payment")
  @JsonView(View.Invoice.class)
  public ResponseEntity<Invoice> requestPaymentFromFranchisee(
      Franchisor franchisor,
      Franchisee franchisee,
      @RequestBody Map<String, Object> body
  ) {
    double due = (double) body.get("due");
    Invoice invoice = Invoice.builder()
        .sender(franchisor)
        .recipient(franchisee)
        .due(due)
        .description("requesting royalty payment")
        .build();
    validatorUtils.validate(invoice);
    transactionService.saveInvoice(invoice);
    return new ResponseEntity<>(invoice, HttpStatus.OK);
  }

  //	Auth: franchisees.getIncomeStatements . contains incomeStatement
  @PostMapping("/{franchisorId}/franchisees/{franchiseeId}/request-fees/financials/{incomeStatementId}")
  @JsonView(View.Summary.class)
  public ResponseEntity<Collection<PercentageFee>> requestMonthlyFranchiseFees(
      Franchisor franchisor,
      Franchisee franchisee,
      IncomeStatement incomeStatement
  ) {
    List<PercentageFee> fees = franchiseService.createMonthlyFranchiseFees(incomeStatement);
    return new ResponseEntity<>(fees, HttpStatus.OK);
  }

  // get fees payments
  @GetMapping("{franchisorId}/fees")
  @JsonView(View.Summary.class)
  public ResponseEntity<Collection<PercentageFee>> getFees(
      Franchisor franchisor,
      @RequestParam(name = "fee-type", required = false) FeeType type,
      //see if we can refactor fullyPaid
      @RequestParam(required = false, name = "fully-paid") final Boolean isFullyPaid
  ) {
    List<PercentageFee> fees = franchiseService.getPercentageFees(franchisor);
    fees = isFullyPaid == null ? fees : filterPercentageFeesByFullyPaid(fees, isFullyPaid);
    if (type != null) {
      fees = fees.stream()
          .filter(percentageFee -> percentageFee.getFeeType().equalsIgnoreCase(type.toString()))
          .collect(
              Collectors.toList());
    }
    return new ResponseEntity<>(fees, HttpStatus.OK);


  }

  private List<PercentageFee> filterPercentageFeesByFullyPaid(List<PercentageFee> fees,
      boolean isFullyPaid) {
    fees = fees.stream()
        .filter(fee -> fee.getInvoice().isFullyPaid() == isFullyPaid)
        .collect(Collectors.toList());
    return fees;
  }
}
