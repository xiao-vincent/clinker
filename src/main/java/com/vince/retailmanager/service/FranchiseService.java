package com.vince.retailmanager.service;

import com.vince.retailmanager.entity.Company;
import com.vince.retailmanager.entity.Franchisee;
import com.vince.retailmanager.entity.Franchisor;
import com.vince.retailmanager.web.exception.EntityNotFoundException;

import java.util.List;

public interface FranchiseService {
//	void saveCompany(Franchisor franchisor);

	void saveFranchisee(Franchisee franchisee);

	void saveCompany(Company company);

	Franchisor findFranchisorById(int id) throws EntityNotFoundException;

	void disableFranchisor(Franchisor franchisor) throws Exception;

	void disableCompany(Company company);

	List<Franchisor> findAllFranchisors();


	Franchisee findFranchiseeById(int franchiseeId) throws EntityNotFoundException;
}
