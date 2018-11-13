package com.vince.retailmanager.web;

import com.vince.retailmanager.entity.AccessToken;
import com.vince.retailmanager.entity.Franchisee;
import com.vince.retailmanager.entity.Franchisor;
import com.vince.retailmanager.entity.User;
import com.vince.retailmanager.mapper.EntityObjectMampper;
import com.vince.retailmanager.service.FranchiseService;
import com.vince.retailmanager.service.UserService;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import javax.xml.ws.http.HTTPException;
import java.util.Map;

@RestController
@RequestMapping(value = {"/franchisors/"})
public class FranchisorController {
	@Autowired
	private FranchiseService franchiseService;
	@Autowired
	public UserService userService;

	@ModelAttribute
	public void populateModel(Model model,
	                          @PathVariable(value = "id", required = false) Integer id
	) {
		if (id == null) return;

		model.addAttribute("franchisor", franchiseService.findFranchisorById(id));
		model.addAttribute("accessToken", userService.findAccessToken(id));
	}

	@GetMapping("{id}")
	public Franchisor findFranchisor(Franchisor franchisor) {
		return franchisor;
	}

	@PostMapping("new")
	public Franchisor createCompany(@Valid @RequestBody Franchisor franchisor) {
		if (franchisor == null) {
			System.out.println("null..");


			return null;
		}
//		if (franchiseService.findFranchisorById(franchisor.getId()) != null) {

//			// refactor, should return 'already exists' error
//			return null;
//		}
		franchiseService.saveFranchisor(franchisor);
		return franchisor;
	}

	@PostMapping("{id}/franchisees/new")
	//find access token using [Company ID] and get corresponding username
	@PreAuthorize("authentication.name == #accessToken.getUser().getUsername()")
	public ResponseEntity<?> createFranchisee(Franchisor franchisor,
	                                          AccessToken accessToken,
	                                          @RequestBody Map<String, Object> body,
	                                          BindingResult bindingResult,
	                                          ServletUriComponentsBuilder ucBuilder
	) {
		System.out.println(accessToken.getUser());
		String username = (String) body.get("username");
		User user = userService.findUser(username);
		BindingErrorsResponse errors = new BindingErrorsResponse();
		HttpHeaders headers = new HttpHeaders();

		if (bindingResult.hasErrors() || (user == null)) {
			errors.addAllErrors(bindingResult);
			headers.add("errors", errors.toJSON());
			return new ResponseEntity<>(errors.toJSON(), HttpStatus.BAD_REQUEST);
		}
		Franchisee franchisee = Franchisee.builder().build();
		franchisor.addFranchisee(franchisee);
		this.franchiseService.saveFranchisee(franchisee);
		headers.setLocation(ServletUriComponentsBuilder.fromCurrentContextPath().buildAndExpand(franchisee.getId()).toUri());
		return new ResponseEntity<>(franchisee, headers, HttpStatus.CREATED);
	}

	@PutMapping("{id}")
	public Franchisor updateCompany(@PathVariable("id") int id,
	                                @Valid @RequestBody Franchisor franchisorRequest) {
		Franchisor franchisorModel = retrieveFranchisor(id);
		EntityObjectMampper mapper = Mappers.getMapper(EntityObjectMampper.class);
		franchisorModel = mapper.sourceToDestination(franchisorRequest, franchisorModel);
		franchiseService.saveFranchisor(franchisorModel);
		return franchisorModel;
	}

//    @DeleteMapping("/{id")
//    public ResponseEntity<?> deleteCompany(@PathVariable("id") int id) {
//        return franchiseService.findCompanyById(id);
//    }


	private Franchisor retrieveFranchisor(int id) {
		Franchisor franchisor = franchiseService.findFranchisorById(id);
		if (franchisor == null) {
			System.out.println("hi");
			throw new HTTPException(403);
		}
		return franchisor;
	}


}
