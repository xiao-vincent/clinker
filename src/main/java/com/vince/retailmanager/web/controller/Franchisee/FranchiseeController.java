package com.vince.retailmanager.web.controller.Franchisee;

import com.fasterxml.jackson.annotation.JsonView;
import com.vince.retailmanager.entity.Franchisee;
import com.vince.retailmanager.entity.View;
import com.vince.retailmanager.service.FranchiseService;
import com.vince.retailmanager.service.PaymentService;
import com.vince.retailmanager.service.UserService;
import com.vince.retailmanager.web.controller.utils.ControllerUtils;
import com.vince.retailmanager.web.exception.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Validator;


@RestController
@RequestMapping("/{franchisorId}/franchisees/{franchiseeId}")
public class FranchiseeController {

	@Autowired
	private FranchiseService franchiseService;

	@Autowired
	public UserService userService;

	@Autowired
	public PaymentService paymentService;


	@Autowired
	public Validator validator;

	@ModelAttribute
	public void populateModel(
		 Model model,
		 @AuthenticationPrincipal org.springframework.security.core.userdetails.User authenticatedUser,
		 @PathVariable("franchisorId") Integer franchisorId,
		 @PathVariable("franchiseeId") Integer franchiseeId
	) throws EntityNotFoundException {
		if (franchisorId == null || franchiseeId == null) return;
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


//	@GetMapping("/hi")
//	@PreAuthorize("authentication.name == 'admin'")
//	public Set<String> hello(Franchisor franchisor, ServletUriComponentsBuilder ucBuilder) {
//		System.out.println(ucBuilder.fromCurrentRequest());
//		System.out.println(ucBuilder.buildAndExpand().getPath());
//		System.out.println(ServletUriComponentsBuilder.fromCurrentRequest().build());
//		System.out.println(ServletUriComponentsBuilder.fromCurrentRequestUri().build());
//		System.out.println(ServletUriComponentsBuilder.fromCurrentContextPath().build());
//
//		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//		System.out.println(authentication);
//		System.out.println(franchisor.getDescription());
//		Set<String> set = new HashSet<>(Arrays.asList("admin", "b"));
//		return set;
//	}


//    @GetMapping("/{franchisorId}")
//    public Franchisor findCompany(@PathVariable("franchisorId") int franchisorId) {
//        Optional<Franchisor> franchisorOptional = franchiseService.findFranchisorById(franchisorId);
//        if (franchisorOptional.isPresent()) return franchisorOptional.get();
//
//        return null;
//    }

//    @PostMapping
//    public Franchisor createCompany(@Valid @RequestBody Franchisor franchisor) {
//        System.out.println(franchisor);
//        if (franchiseService.findFranchisorById(franchisor.getId()).isPresent()) {
//            // refactor, should return 'already exists' error
//            return null;
//        }
//        franchiseService.saveCompany(franchisor);
//        return franchisor;
//    }
//
//    @PutMapping("/{franchisorId}")
//    public Franchisor updateCompany(@PathVariable("franchisorId") int franchisorId,
//                                    @Valid @RequestBody Franchisor franchisorRequest) {
//        Franchisor franchisorModel = retrieveCompany(franchisorId);
//        EntityObjectMampper mapper = Mappers.getMapper(EntityObjectMampper.class);
//        franchisorModel = mapper.sourceToDestination(franchisorRequest, franchisorModel);
//        franchiseService.saveCompany(franchisorModel);
//        return franchisorModel;
//    }

//    @DeleteMapping("/{franchisorId")
//    public ResponseEntity<?> deleteCompany(@PathVariable("franchisorId") int franchisorId) {
//        return franchiseService.findCompanyById(franchisorId);
//    }

//let's make some crud

//    private Franchisor retrieveCompany(int franchisorId) {
//        Optional<Franchisor> franchisorOptional = this.franchiseService.findFranchisorById(franchisorId);
//        if (franchisorOptional.isPresent()) {
//            return franchisorOptional.get();
//        }
//        throw new HTTPException(403);
//    }


}
