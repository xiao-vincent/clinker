package com.vince.retailmanager.web;

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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import javax.xml.ws.http.HTTPException;
import java.util.Map;

@RestController
@RequestMapping("/franchisors")
public class FranchisorController {
	public static final String id = "/{franchisorId}";
	@Autowired
	private FranchiseService franchiseService;
	@Autowired
	private UserService userService;

	@ModelAttribute("franchisor")
	public Franchisor addFranchisorToModel(@PathVariable("franchisorId") int franchisorId) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return franchiseService.findFranchisorById(franchisorId);
	}

	@GetMapping(id)
	public Franchisor findFranchisor(@PathVariable("franchisorId") int franchisorId) {
		return retrieveFranchisor(franchisorId);
	}

	@PostMapping("/new")
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

	@PostMapping(id + "/franchisees/new")
//	@PreAuthorize("authentication.name == #franchisor.getUser().getUsername()")
	public ResponseEntity<?> createFranchisee(Franchisor franchisor,
	                                          @RequestBody Map<String, Object> body,
	                                          BindingResult bindingResult,
	                                          ServletUriComponentsBuilder ucBuilder
	) {

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
		headers.setLocation(ucBuilder.fromCurrentContextPath().buildAndExpand(franchisee.getId()).toUri());
		return new ResponseEntity<>(franchisee, headers, HttpStatus.CREATED);
	}

	@PutMapping(id)
	public Franchisor updateCompany(@PathVariable("franchisorId") int franchisorId,
	                                @Valid @RequestBody Franchisor franchisorRequest) {
		Franchisor franchisorModel = retrieveFranchisor(franchisorId);
		EntityObjectMampper mapper = Mappers.getMapper(EntityObjectMampper.class);
		franchisorModel = mapper.sourceToDestination(franchisorRequest, franchisorModel);
		franchiseService.saveFranchisor(franchisorModel);
		return franchisorModel;
	}

//    @DeleteMapping("/{franchisorId")
//    public ResponseEntity<?> deleteCompany(@PathVariable("franchisorId") int franchisorId) {
//        return franchiseService.findCompanyById(franchisorId);
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
