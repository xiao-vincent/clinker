package com.vince.retailmanager.service;

import com.vince.retailmanager.model.entity.companies.Company;
import com.vince.retailmanager.model.entity.companies.Franchisee;
import com.vince.retailmanager.model.entity.companies.Franchisor;
import com.vince.retailmanager.model.entity.fees.FeeType;
import com.vince.retailmanager.model.entity.fees.MarketingFee;
import com.vince.retailmanager.model.entity.fees.PercentageFee;
import com.vince.retailmanager.model.entity.fees.Royalty;
import com.vince.retailmanager.model.entity.financials.IncomeStatement;
import com.vince.retailmanager.exception.EntityNotFoundException;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

public interface FranchiseService {
//	void saveCompany(Franchisor franchisor);

  Franchisor saveFranchisor(Franchisor franchisor);

  Franchisee saveFranchisee(Franchisee franchisee);

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
  List<PercentageFee> getPercentageFees(Franchisor franchisor, FeeType type);
}
