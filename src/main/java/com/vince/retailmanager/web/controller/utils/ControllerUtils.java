package com.vince.retailmanager.web.controller.utils;

import com.vince.retailmanager.model.entity.AccessToken;
import com.vince.retailmanager.model.entity.Company;
import com.vince.retailmanager.service.FinancialService;
import com.vince.retailmanager.service.FranchiseService;
import com.vince.retailmanager.service.UserService;
import com.vince.retailmanager.web.exception.EntityNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

@Component
public class ControllerUtils {

  private FranchiseService franchiseService;
  private FinancialService financialService;
  private UserService userService;
  private String activeUsername;

  public ControllerUtils(FranchiseService franchiseService,
      FinancialService financialService, UserService userService) {
    this.franchiseService = franchiseService;
    this.financialService = financialService;
    this.userService = userService;
  }

  public static void addActiveUsername(
      Model model,
      @AuthenticationPrincipal User authenticatedUser,
      Integer id,
      UserService userService
  ) {
    String authenticatedUsername = authenticatedUser.getUsername();
    AccessToken accessToken = userService.findAccessToken(authenticatedUsername, id);
    if (accessToken != null) {
      model.addAttribute("activeUsername", accessToken.getUser().getUsername());
    }
  }

  public boolean hasAccessTo(
      Company company
  ) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    AccessToken accessToken = userService
        .findAccessToken(auth.getName(), company.getId());
    return accessToken != null;
  }

  public void addUser(Model model) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String username = authentication.getName();
    com.vince.retailmanager.model.entity.User user = userService.findUser(username);
    model.addAttribute("user", user);
  }

  public void addUsername(
      Model model,
      @AuthenticationPrincipal User authenticatedUser,
      Integer id,
      UserService userService
  ) {
    String authenticatedUsername = authenticatedUser.getUsername();
    AccessToken accessToken = userService.findAccessToken(authenticatedUsername, id);
    if (accessToken != null) {
//      model.addAttribute("activeUsername", accessToken.getUser().getUsername());
      this.activeUsername = accessToken.getUser().getUsername();
    }
  }


  public boolean fakeFunc() {
    System.out.println("hello");
    return true;
  }


  public void addFranchiseeToModel(Model model, Integer franchiseeId)
      throws EntityNotFoundException {
    if (franchiseeId != null) {
      model.addAttribute("franchisee", franchiseService.findFranchiseeById(franchiseeId));
    }
  }

  public void addFranchisorToModel(Model model, Integer franchisorId)
      throws EntityNotFoundException {
    if (franchisorId != null) {
      model.addAttribute("franchisor", franchiseService.findFranchisorById(franchisorId));
    }
  }

  public void addIncomeStatementToModel(Model model, Integer incomeStatementId)
      throws EntityNotFoundException {
    if (incomeStatementId != null) {
      model.addAttribute("incomeStatement",
          financialService.findIncomeStatementById(incomeStatementId));
    }
  }


}
