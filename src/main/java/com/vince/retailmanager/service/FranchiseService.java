package com.vince.retailmanager.service;

import com.vince.retailmanager.entity.Franchisee;
import com.vince.retailmanager.entity.Franchisor;
import com.vince.retailmanager.entity.Payment;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface FranchiseService {
	/*

		x findFranchisorById
		- findFranchiseeById
		- findAllFranchisors

	 */

	void saveFranchisor(Franchisor franchisor);

	void saveFranchisee(Franchisee franchisee);


	Franchisor findFranchisorById(int id);

	List<Franchisor> findAllFranchisors();


	@Transactional
	void savePayment(Payment payment);

	Franchisee findFranchiseeById(int franchiseeId);
}
