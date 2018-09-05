package com.vince.retailmanager.service;

import com.vince.retailmanager.entity.Franchisee;
import com.vince.retailmanager.entity.Franchisor;

public interface BusinessService {

    void saveFranchisor(Franchisor franchisor);

    void saveFranchisee(Franchisee franchisee);

    Franchisor findFranchisorById(int id);


}
