package com.vince.retailmanager.service;

import com.vince.retailmanager.entity.Company;
import com.vince.retailmanager.entity.Franchisee;
import com.vince.retailmanager.entity.Franchisor;
import com.vince.retailmanager.entity.Payment;
import com.vince.retailmanager.web.EntityNotFoundException;

import java.util.List;

public interface FranchiseService {
	void saveFranchisor(Franchisor franchisor);

	void saveFranchisee(Franchisee franchisee);

	Franchisor findFranchisorById(int id) throws EntityNotFoundException;

	void disableFranchisor(Franchisor franchisor) throws Exception;

	void disableCompany(Company company);


	List<Franchisor> findAllFranchisors();

	void savePayment(Payment payment);

	Franchisee findFranchiseeById(int franchiseeId);
}
