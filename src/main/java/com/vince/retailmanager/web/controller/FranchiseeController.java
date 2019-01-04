package com.vince.retailmanager.web.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.vince.retailmanager.model.View;
import com.vince.retailmanager.model.entity.Franchisee;
import com.vince.retailmanager.service.FranchiseService;
import com.vince.retailmanager.service.UserService;
import com.vince.retailmanager.web.controller.utils.ControllerUtils;
import com.vince.retailmanager.web.exception.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/{franchisorId}/franchisees/{franchiseeId}")
public class FranchiseeController {

  @Autowired
  public UserService userService;
  @Autowired
  private FranchiseService franchiseService;
  @Autowired
  private ControllerUtils controllerUtils;

  @ModelAttribute
  public void populateModel(
      Model model,
      @AuthenticationPrincipal org.springframework.security.core.userdetails.User authenticatedUser,
      @PathVariable("franchisorId") Integer franchisorId,
      @PathVariable("franchiseeId") Integer franchiseeId
  ) throws EntityNotFoundException {
    if (franchisorId == null || franchiseeId == null) {
      return;
    }
    ControllerUtils.addActiveUsername(model, authenticatedUser, franchiseeId, userService);

    model.addAttribute("franchisor", franchiseService.findFranchisorById(franchisorId));
    model.addAttribute("franchisee", franchiseService.findFranchiseeById(franchiseeId));
  }

  @GetMapping
  @JsonView(View.Franchisee.class)
  public ResponseEntity<Franchisee> getFranchisee(Franchisee franchisee) {
    return new ResponseEntity<>(franchisee, HttpStatus.OK);
  }

}
