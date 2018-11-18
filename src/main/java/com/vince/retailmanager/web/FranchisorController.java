package com.vince.retailmanager.web;

import com.vince.retailmanager.entity.AccessToken;
import com.vince.retailmanager.entity.Franchisee;
import com.vince.retailmanager.entity.Franchisor;
import com.vince.retailmanager.entity.User;
import com.vince.retailmanager.service.FranchiseService;
import com.vince.retailmanager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.xml.ws.http.HTTPException;

@RestController
@RequestMapping(value = {"/franchisors/"})
public class FranchisorController {
	@Autowired
	private FranchiseService franchiseService;
	@Autowired
	public UserService userService;

	@ModelAttribute
	public void populateModel(Model model,
	                          @AuthenticationPrincipal org.springframework.security.core.userdetails.User user,
	                          @PathVariable(value = "id", required = false) Integer id
	) throws EntityNotFoundException {
		System.out.println("username:" + user.getUsername());
		System.out.println("user:" + userService.findUser(user.getUsername()));
		model.addAttribute("activeUser", userService.findUser(user.getUsername()));
		if (id == null) return;
		model.addAttribute("franchisor", franchiseService.findFranchisorById(id));
		model.addAttribute("accessToken", userService.findAccessToken(id));

	}

	@GetMapping("{id}")
	public ResponseEntity<Franchisor> findFranchisor(Franchisor franchisor) {
		return new ResponseEntity<>(franchisor, HttpStatus.OK);
	}

	@PostMapping("new")
	public ResponseEntity<Franchisor> createCompany(@Valid @RequestBody Franchisor franchisor,
	                                                @ModelAttribute("activeUser") User activeUser
	) {
		activeUser.addAccessToken(franchisor);
		try {
			franchiseService.saveFranchisor(franchisor);
		} catch (DataIntegrityViolationException e) {
			System.out.println(franchiseService.isValid(franchisor));
		}
		return new ResponseEntity<>(franchisor, HttpStatus.CREATED);
	}

	@PostMapping("{id}/franchisees/new")
	//find access token using [Company ID] and get corresponding username
	@PreAuthorize("authentication.name == #accessToken.getUser().getUsername()")
	public ResponseEntity<?> createFranchisee(Franchisor franchisor,
	                                          AccessToken accessToken
	) {
		Franchisee franchisee = Franchisee.builder().build();
		franchisor.addFranchisee(franchisee);
		this.franchiseService.saveFranchisee(franchisee);
		return new ResponseEntity<>(franchisee, HttpStatus.CREATED);
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

//    @DeleteMapping("/{id")
//    public ResponseEntity<?> deleteCompany(@PathVariable("id") int id) {
//        return franchiseService.findCompanyById(id);
//    }


	private Franchisor retrieveFranchisor(int id) throws EntityNotFoundException {
		Franchisor franchisor = franchiseService.findFranchisorById(id);
		if (franchisor == null) {
			System.out.println("hi");
			throw new HTTPException(403);
		}
		return franchisor;
	}


}
