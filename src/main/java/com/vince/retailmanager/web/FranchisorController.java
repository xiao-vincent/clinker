package com.vince.retailmanager.web;

import com.vince.retailmanager.entity.Franchisor;
import com.vince.retailmanager.mapper.EntityObjectMampper;
import com.vince.retailmanager.service.BusinessService;
import org.mapstruct.factory.Mappers;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.xml.ws.http.HTTPException;

@RestController
@RequestMapping("/franchisors")
public class FranchisorController {
    private final BusinessService businessService;

    public FranchisorController(BusinessService businessService) {
        this.businessService = businessService;
    }

    @GetMapping("/{franchisorId}")
    public Franchisor findFranchisor(@PathVariable("franchisorId") int franchisorId) {
        return retrieveFranchisor(franchisorId);
    }

    @PostMapping("/new")
    public Franchisor createCompany(@Valid @RequestBody Franchisor franchisor) {
        if (businessService.findFranchisorById(franchisor.getId()) != null) {
            // refactor, should return 'already exists' error
            return null;
        }
        businessService.saveFranchisor(franchisor);
        return franchisor;
    }

    @PutMapping("/{franchisorId}")
    public Franchisor updateCompany(@PathVariable("franchisorId") int franchisorId,
                                    @Valid @RequestBody Franchisor franchisorRequest) {
        Franchisor franchisorModel = retrieveFranchisor(franchisorId);
        EntityObjectMampper mapper = Mappers.getMapper(EntityObjectMampper.class);
        franchisorModel = mapper.sourceToDestination(franchisorRequest, franchisorModel);
        businessService.saveFranchisor(franchisorModel);
        return franchisorModel;
    }

//    @DeleteMapping("/{franchisorId")
//    public ResponseEntity<?> deleteCompany(@PathVariable("franchisorId") int franchisorId) {
//        return businessService.findCompanyById(franchisorId);
//    }


    private Franchisor retrieveFranchisor(int id) {
        Franchisor franchisor = businessService.findFranchisorById(id);
        if (franchisor == null) {
            System.out.println("hi");
            throw new HTTPException(403);
        }
        return franchisor;
    }


}
