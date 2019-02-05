package com.vince.retailmanager.service;

import com.vince.retailmanager.exception.EntityNotFoundException;
import com.vince.retailmanager.model.entity.companies.Company;
import com.vince.retailmanager.model.entity.companies.Franchisee;
import com.vince.retailmanager.model.entity.companies.Franchisor;
import com.vince.retailmanager.model.entity.fees.FeeType;
import com.vince.retailmanager.model.entity.fees.PercentageFee;
import com.vince.retailmanager.model.entity.financials.IncomeStatement;
import java.util.List;

public interface FranchiseService {

  Franchisor saveFranchisor(Franchisor franchisor);

  Franchisee saveFranchisee(Franchisee franchisee);

  Company saveCompany(Company company);

  Company findCompanyById(int id) throws EntityNotFoundException;

  Franchisor findFranchisorById(int id) throws EntityNotFoundException;

  void disableFranchisor(Franchisor franchisor) throws Exception;

  void disableCompany(Company company);


  Franchisee findFranchiseeById(int franchiseeId) throws EntityNotFoundException;

  PercentageFee savePercentageFee(PercentageFee percentageFee);

  List<PercentageFee> createMonthlyFranchiseFees(IncomeStatement incomeStatement);

  List<PercentageFee> getPercentageFees(Franchisor franchisor, FeeType type);
}
