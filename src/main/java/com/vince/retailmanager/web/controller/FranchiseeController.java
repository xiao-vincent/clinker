package com.vince.retailmanager.web.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.vince.retailmanager.exception.EntityNotFoundException;
import com.vince.retailmanager.model.entity.companies.Franchisee;
import com.vince.retailmanager.service.UserService;
import com.vince.retailmanager.web.json.View;
import com.vince.retailmanager.web.utils.ModelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
  private ModelUtils modelUtils;

  @ModelAttribute
  public void populateModel(
      Model model,
      @PathVariable("franchisorId") Integer franchisorId,
      @PathVariable("franchiseeId") Integer franchiseeId
  ) throws EntityNotFoundException {
    modelUtils.setModel(model);
    modelUtils.addFranchisor(franchisorId);
    modelUtils.addFranchisee(franchiseeId);
  }

  @GetMapping
  @JsonView(View.Franchisee.class)
  public ResponseEntity<Franchisee> getFranchisee(Franchisee franchisee) {
    return new ResponseEntity<>(franchisee, HttpStatus.OK);
  }

}
