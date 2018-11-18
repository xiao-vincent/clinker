package com.vince.retailmanager.service;

import com.vince.retailmanager.entity.Franchisee;
import com.vince.retailmanager.entity.Franchisor;
import com.vince.retailmanager.entity.Payment;
import com.vince.retailmanager.web.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface FranchiseService {
	void saveFranchisor(Franchisor franchisor);

	void saveFranchisee(Franchisee franchisee);

	boolean isValid(Franchisor franchisor);


	Franchisor findFranchisorById(int id) throws EntityNotFoundException;

	List<Franchisor> findAllFranchisors();


	@Transactional
	void savePayment(Payment payment);

	Franchisee findFranchiseeById(int franchiseeId);
}
