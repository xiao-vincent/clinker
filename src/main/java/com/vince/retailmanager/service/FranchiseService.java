package com.vince.retailmanager.service;

import com.vince.retailmanager.entity.*;
import com.vince.retailmanager.web.exception.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface FranchiseService {
//	void saveCompany(Franchisor franchisor);

	Franchisor saveFranchisor(Franchisor franchisor);

	void saveFranchisee(Franchisee franchisee);

	Company saveCompany(Company company);

	Company findCompanyById(int id) throws EntityNotFoundException;

	Franchisor findFranchisorById(int id) throws EntityNotFoundException;

	void disableFranchisor(Franchisor franchisor) throws Exception;

	void disableCompany(Company company);

	List<Franchisor> findAllFranchisors();

	Franchisee findFranchiseeById(int franchiseeId) throws EntityNotFoundException;

	Royalty saveRoyalty(Royalty royalty);

	MarketingFee saveMarketingFee(MarketingFee marketingFee);

	PercentageFee savePercentageFee(PercentageFee percentageFee);


	@Transactional
	List<PercentageFee> createMonthlyFranchiseFees(IncomeStatement incomeStatement);

	@Transactional
	List<PercentageFee> getPercentageFees(Franchisor franchisor);
}
