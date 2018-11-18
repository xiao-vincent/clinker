package com.vince.retailmanager.web;

import com.vince.retailmanager.entity.Franchisee;
import com.vince.retailmanager.entity.Payment;
import com.vince.retailmanager.service.FranchiseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;


@RestController
@CrossOrigin(exposedHeaders = "errors, content-type")
@RequestMapping("/franchisor/{franchisorId}/franchisees/{franchiseeId}")
public class FranchiseeController {
	@Autowired
	private FranchiseService franchiseService;

	@Autowired
	private HttpServletRequest request;


//	@ModelAttribute("franchisor")
//	public Franchisor findFranchisor(@PathVariable("franchisorId") int franchisorId) {
//		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//		return franchiseService.findFranchisorById(franchisorId);
//	}

	@ModelAttribute("franchisee")
	public Franchisee findFranchisee(@PathVariable("franchiseeId") int franchiseeId) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return franchiseService.findFranchiseeById(franchiseeId);
	}

	@InitBinder("franchisor")
	public void initOwnerBinder(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@InitBinder("franchisee")
	public void initFranchiseeBinder(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@PostMapping("/pay/{amount}")
	@PreAuthorize("isLoggedIn(#franchisee.getUser())")
	@Validated
	public ResponseEntity<String> makePaymentToFranchisor(@PathVariable @Positive Double amount, Franchisee franchisee) {
		BindingErrorsResponse errors = new BindingErrorsResponse();
		HttpHeaders headers = new HttpHeaders();
		headers.add("errors", errors.toJSON());
		if (isValidCurrency(amount)) return new ResponseEntity<>(headers, HttpStatus.BAD_REQUEST);
		Payment payment = Payment.builder()
			 .payer(franchisee)
			 .recipient(franchisee.getFranchisor())
			 .amount(amount)
			 .build();
		franchiseService.savePayment(payment);
		return null;
	}

	private boolean isValidCurrency(Double amount) {
		return amount <= 0.0;
	}

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
//        franchiseService.saveFranchisor(franchisor);
//        return franchisor;
//    }
//
//    @PutMapping("/{franchisorId}")
//    public Franchisor updateCompany(@PathVariable("franchisorId") int franchisorId,
//                                    @Valid @RequestBody Franchisor franchisorRequest) {
//        Franchisor franchisorModel = retrieveCompany(franchisorId);
//        EntityObjectMampper mapper = Mappers.getMapper(EntityObjectMampper.class);
//        franchisorModel = mapper.sourceToDestination(franchisorRequest, franchisorModel);
//        franchiseService.saveFranchisor(franchisorModel);
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
