package com.vince.retailmanager.service;

import com.vince.retailmanager.entity.Franchisee;
import com.vince.retailmanager.entity.Franchisor;
import com.vince.retailmanager.repository.FranchiseeRepository;
import com.vince.retailmanager.repository.FranchisorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class BusinessServiceImpl implements BusinessService {
    private FranchisorRepository franchisorRepository;
    private FranchiseeRepository franchiseeRepository;

    @Autowired
    public BusinessServiceImpl(FranchisorRepository franchisorRepository, FranchiseeRepository franchiseeRepository) {
        this.franchisorRepository = franchisorRepository;
        this.franchiseeRepository = franchiseeRepository;
    }


    @Override
    @Transactional
    public void saveFranchisor(Franchisor franchisor) {
        franchisorRepository.save(franchisor);
    }

    @Override
    @Transactional(readOnly = true)
    public Franchisor findFranchisorById(int id) {
        Optional<Franchisor> optionalFranchisor = franchisorRepository.findById(id);
//        if (optionalFranchisor.isPresent()) {
//            return (Franchisor) optionalFranchisor.get();
//        } else {
//            //refactor
//            return null;
//        }
        return optionalFranchisor.map(f -> f).orElse(null);
    }

    @Override
    @Transactional
    public void saveFranchisee(Franchisee franchisee) {
        franchiseeRepository.save(franchisee);
    }
}
