package com.vince.retailmanager.web;

import com.vince.retailmanager.entity.AccessToken;
import com.vince.retailmanager.entity.Franchisee;
import com.vince.retailmanager.entity.Franchisor;
import com.vince.retailmanager.entity.User;
import com.vince.retailmanager.service.FranchiseService;
import com.vince.retailmanager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.Validator;

@RestController
@RequestMapping(value = {"/franchisors/"})
public class FranchisorController {
	@Autowired
	private FranchiseService franchiseService;
	@Autowired
	public UserService userService;
	@Autowired
	public Validator validator;

	@ModelAttribute
	public void populateModel(Model model,
	                          @AuthenticationPrincipal org.springframework.security.core.userdetails.User authenticatedUser,
	                          @PathVariable(value = "id", required = false) Integer id
	) throws EntityNotFoundException {
		String authenticatedUsername = authenticatedUser.getUsername();
		model.addAttribute("user", userService.findUser(authenticatedUsername));
		if (id == null) return;
		model.addAttribute("franchisor", franchiseService.findFranchisorById(id));
		AccessToken accessToken = userService.findAccessToken(authenticatedUsername, id);
//		model.addAttribute("accessToken", accessToken);
		if (accessToken != null) {
			model.addAttribute("activeUsername", accessToken.getUser().getUsername());
		}

//		if (accessToken != null) {
//			eligibleUsername = accessToken.getUser().getUsername();
//		}
//		model.addAttribute(" ", );


	}

	@GetMapping("{id}")
	public ResponseEntity<Franchisor> findFranchisor(Franchisor franchisor) {
		return new ResponseEntity<>(franchisor, HttpStatus.OK);
	}

	@PostMapping("new")
	public ResponseEntity<Franchisor> createCompany(@Valid @RequestBody Franchisor franchisor,
	                                                User user
	) {
		user.addAccessToken(franchisor);
		franchiseService.saveFranchisor(franchisor);
		return new ResponseEntity<>(franchisor, HttpStatus.CREATED);
	}

	@PutMapping("{id}")
	public Franchisor updateCompany(@PathVariable("id") int id,
	                                @Valid @RequestBody Franchisor franchisorRequest) {
//		Franchisor franchisorModel = retrieveFranchisor(id);
//		EntityObjectMampper mapper = Mappers.getMapper(EntityObjectMampper.class);
//		franchisorModel = mapper.sourceToDestination(franchisorRequest, franchisorModel);
//		franchiseService.saveFranchisor(franchisorModel);
//		return franchisorModel;
		return null;
	}

	@PostMapping("{id}/franchisees/new")
	@PreAuthorize("authentication.name == #activeUsername")
	public ResponseEntity<?> createFranchisee(Franchisor franchisor,
	                                          @ModelAttribute("activeUsername") String activeUsername
	) {
		Franchisee franchisee = Franchisee.builder().build();
		franchisor.addFranchisee(franchisee);
		this.franchiseService.saveFranchisee(franchisee);
		return new ResponseEntity<>(franchisee, HttpStatus.CREATED);
	}

	@DeleteMapping("{id}")
	@PreAuthorize("authentication.name == #activeUsername")
	public ResponseEntity<Void> deleteFranchisor(Franchisor franchisor,
	                                             @ModelAttribute("activeUsername") String activeUsername
	) throws Exception {
		//custom exception is best here, and catch it in restexcpetionhandler
		franchiseService.disableFranchisor(franchisor);
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}


}
