package com.vince.retailmanager.service;

import static com.vince.retailmanager.utils.StringUtils.singlePlural;

import com.vince.retailmanager.exception.EntityNotFoundException;
import com.vince.retailmanager.exception.InvalidOperationException;
import com.vince.retailmanager.model.entity.companies.Company;
import com.vince.retailmanager.model.entity.companies.Franchisee;
import com.vince.retailmanager.model.entity.companies.Franchisor;
import com.vince.retailmanager.model.entity.fees.FeeType;
import com.vince.retailmanager.model.entity.fees.MarketingFee;
import com.vince.retailmanager.model.entity.fees.PercentageFee;
import com.vince.retailmanager.model.entity.fees.Royalty;
import com.vince.retailmanager.model.entity.financials.IncomeStatement;
import com.vince.retailmanager.repository.CompanyRepository;
import com.vince.retailmanager.repository.FranchiseeRepository;
import com.vince.retailmanager.repository.FranchisorRepository;
import com.vince.retailmanager.repository.PercentageFeeRepository;
import com.vince.retailmanager.utils.ValidatorUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FranchiseServiceImpl implements FranchiseService {

  private CompanyRepository companyRepository;
  private FranchisorRepository franchisorRepository;
  private FranchiseeRepository franchiseeRepository;
  private PercentageFeeRepository percentageFeeRepository;

  private ValidatorUtils validatorUtils;

  @Autowired
  public FranchiseServiceImpl(
      CompanyRepository companyRepository,
      FranchisorRepository franchisorRepository,
      FranchiseeRepository franchiseeRepository,
      PercentageFeeRepository percentageFeeRepository,
      ValidatorUtils validatorUtils) {
    this.companyRepository = companyRepository;
    this.franchisorRepository = franchisorRepository;
    this.franchiseeRepository = franchiseeRepository;
    this.percentageFeeRepository = percentageFeeRepository;
    this.validatorUtils = validatorUtils;
  }


  @Override
  @Transactional
  public <R extends Company> R saveCompany(R company) {
    return companyRepository.save(company);
  }

  @Override
  @Transactional
  public Company findCompanyById(int id) throws EntityNotFoundException {
    Company company = companyRepository.findById(id).orElse(null);
    if (company == null) {
      throw new EntityNotFoundException(Company.class, "id", String.valueOf(id));
    }
    return company;
  }


  private void throwActiveFranchiseesException(Franchisor franchisor, Set<Franchisee> franchisees) {
    Map<String, Set<Franchisee>> invalidValues = new HashMap<>();
    invalidValues.put("franchisees", franchisees);
    String franchisorClassName = franchisor.getClass().getSimpleName();
    String errorMsg = new StringBuilder()
        .append(franchisorClassName)
        .append(" still has ")
        .append(franchisees.size())
        .append(" active ")
        .append(singlePlural(franchisees.size(), "franchisee"))
        .toString();

    throw new InvalidOperationException(
        errorMsg,
        invalidValues
    );
  }

  private void disableCompany(Company company) {
    company.setEnabled(false);
    companyRepository.save(company);
  }

  @Override
  @Transactional
  public void disable(Franchisor franchisor) {
    Set<Franchisee> franchisees = franchisor.getFranchisees();
    if (!franchisees.isEmpty()) {
      throwActiveFranchiseesException(franchisor, franchisees);
    }
    disableCompany(franchisor);
  }

  @Override
  @Transactional
  public PercentageFee savePercentageFee(PercentageFee percentageFee) {
    validatorUtils.validate(percentageFee);
    return percentageFeeRepository.save(percentageFee);
  }


  @Override
  @Transactional
  public List<PercentageFee> createMonthlyFranchiseFees(IncomeStatement incomeStatement) {
    //if incomeStatement.get company is not an instance of Franchisee, throw exception
    List<PercentageFee> fees = new ArrayList<>();
    fees.add(Royalty.create(incomeStatement));
    fees.add(MarketingFee.create(incomeStatement));
    fees.forEach(fee -> savePercentageFee(fee));
    return fees;

  }

  @Override
  @Transactional
  public List<PercentageFee> getPercentageFees(Franchisor franchisor, FeeType type) {
    if (franchisor == null) {
      return Collections.emptyList();
    }
    Set<Integer> franchiseeIds = franchisor.getFranchisees().stream().map(Franchisee::getId)
        .collect(Collectors.toSet());
    if (type != null) {
      return percentageFeeRepository
          .findAllByFranchiseeIdInAndFeeType(franchiseeIds, type.toString());
    } else {
      return percentageFeeRepository.findAllByFranchiseeIdIn(franchiseeIds);
    }

  }
}

