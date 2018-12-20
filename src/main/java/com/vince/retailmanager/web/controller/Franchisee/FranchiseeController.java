package com.vince.retailmanager.web.controller.Franchisee;

import com.fasterxml.jackson.annotation.JsonView;
import com.vince.retailmanager.model.View;
import com.vince.retailmanager.model.entity.Franchisee;
import com.vince.retailmanager.service.FranchiseService;
import com.vince.retailmanager.service.TransactionService;
import com.vince.retailmanager.service.UserService;
import com.vince.retailmanager.web.controller.utils.ControllerUtils;
import com.vince.retailmanager.web.exception.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
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
  public TransactionService transactionService;
  @Autowired
  private FranchiseService franchiseService;

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
    model.addAttribute("franchisor", franchiseService.findFranchisorById(franchisorId));
    model.addAttribute("franchisee", franchiseService.findFranchiseeById(franchiseeId));
    ControllerUtils.addActiveUsername(model, authenticatedUser, franchiseeId, userService);
  }

  @InitBinder("franchisor")
  public void initFranchisorBinder(WebDataBinder dataBinder) {
    dataBinder.setDisallowedFields("id");
  }

  @InitBinder("franchisee")
  public void initFranchiseeBinder(WebDataBinder dataBinder) {
    dataBinder.setDisallowedFields("id");
  }

  @JsonView(View.Franchisee.class)
  @GetMapping
  public ResponseEntity<Franchisee> getFranchiseee(Franchisee franchisee) {
    return new ResponseEntity<>(franchisee, HttpStatus.OK);
  }

//	@GetMapping("/franchisor-info")
//	@JsonView(View.Summary.class)
//	public ResponseEntity<Franchisor> getFranchisor(Franchisor franchisor) {
//		return new ResponseEntity<>(franchisor, HttpStatus.OK);
//	}


}
