package com.vince.retailmanager.web;

import com.vince.retailmanager.entity.Franchisee;
import com.vince.retailmanager.entity.Franchisor;
import com.vince.retailmanager.service.BusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


@RestController
@CrossOrigin(exposedHeaders = "errors, content-type")
@RequestMapping("/franchisor/{franchisorId}/franchisees")
public class FranchiseeController {
    @Autowired
    private BusinessService businessService;

    @Autowired
    private HttpServletRequest request;

    @ModelAttribute("franchisor")
    public Franchisor findFranchisor(@PathVariable("franchisorId") int franchisorId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(authentication);
        return businessService.findFranchisorById(franchisorId);
    }

    @InitBinder("franchisor")
    public void initOwnerBinder(WebDataBinder dataBinder) {
        dataBinder.setDisallowedFields("id");
    }

    @InitBinder("franchisee")
    public void initFranchiseeBinder(WebDataBinder dataBinder) {
        System.out.println("hello");
    }


    @GetMapping("/hi")
    @PreAuthorize("authentication.name == 'admin'")
    public Set<String> hello(Franchisor franchisor, ServletUriComponentsBuilder ucBuilder) {
        System.out.println(ucBuilder.fromCurrentRequest());
        System.out.println(ucBuilder.buildAndExpand().getPath());
        System.out.println(ServletUriComponentsBuilder.fromCurrentRequest().build());
        System.out.println(ServletUriComponentsBuilder.fromCurrentRequestUri().build());
        System.out.println(ServletUriComponentsBuilder.fromCurrentContextPath().build());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(authentication);
        System.out.println(franchisor.getName());
        Set<String> set = new HashSet<>(Arrays.asList("admin", "b"));
        return set;
    }


    @PostMapping("/new")
    @PreAuthorize("authentication.name == #franchisor.getUser().getUsername()")
    public ResponseEntity<Franchisee> createFranchisee(Franchisor franchisor,
                                                       @RequestBody @Valid Franchisee franchisee,
                                                       BindingResult bindingResult,
                                                       ServletUriComponentsBuilder ucBuilder
    ) {
        BindingErrorsResponse errors = new BindingErrorsResponse();
        HttpHeaders headers = new HttpHeaders();
        if (bindingResult.hasErrors() || (franchisee == null)) {
            errors.addAllErrors(bindingResult);
            headers.add("errors", errors.toJSON());
            return new ResponseEntity<Franchisee>(headers, HttpStatus.BAD_REQUEST);
        }
        franchisor.addFranchisee(franchisee);
        this.businessService.saveFranchisee(franchisee);
        headers.setLocation(ucBuilder.fromCurrentContextPath().buildAndExpand(franchisee.getId()).toUri());
        return new ResponseEntity<Franchisee>(franchisee, headers, HttpStatus.CREATED);
    }


//    @GetMapping("/{franchisorId}")
//    public Franchisor findCompany(@PathVariable("franchisorId") int franchisorId) {
//        Optional<Franchisor> franchisorOptional = businessService.findFranchisorById(franchisorId);
//        if (franchisorOptional.isPresent()) return franchisorOptional.get();
//
//        return null;
//    }

//    @PostMapping
//    public Franchisor createCompany(@Valid @RequestBody Franchisor franchisor) {
//        System.out.println(franchisor);
//        if (businessService.findFranchisorById(franchisor.getId()).isPresent()) {
//            // refactor, should return 'already exists' error
//            return null;
//        }
//        businessService.saveFranchisor(franchisor);
//        return franchisor;
//    }
//
//    @PutMapping("/{franchisorId}")
//    public Franchisor updateCompany(@PathVariable("franchisorId") int franchisorId,
//                                    @Valid @RequestBody Franchisor franchisorRequest) {
//        Franchisor franchisorModel = retrieveCompany(franchisorId);
//        EntityObjectMampper mapper = Mappers.getMapper(EntityObjectMampper.class);
//        franchisorModel = mapper.sourceToDestination(franchisorRequest, franchisorModel);
//        businessService.saveFranchisor(franchisorModel);
//        return franchisorModel;
//    }

//    @DeleteMapping("/{franchisorId")
//    public ResponseEntity<?> deleteCompany(@PathVariable("franchisorId") int franchisorId) {
//        return businessService.findCompanyById(franchisorId);
//    }

//let's make some crud

//    private Franchisor retrieveCompany(int franchisorId) {
//        Optional<Franchisor> franchisorOptional = this.businessService.findFranchisorById(franchisorId);
//        if (franchisorOptional.isPresent()) {
//            return franchisorOptional.get();
//        }
//        throw new HTTPException(403);
//    }


}
