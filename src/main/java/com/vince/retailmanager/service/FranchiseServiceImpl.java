package com.vince.retailmanager.service;

import static com.vince.retailmanager.utils.StringUtils.singlePlural;

import com.vince.retailmanager.exception.InvalidOperationException;
import com.vince.retailmanager.model.entity.Company;
import com.vince.retailmanager.model.entity.Franchisee;
import com.vince.retailmanager.model.entity.Franchisor;
import com.vince.retailmanager.model.entity.IncomeStatement;
import com.vince.retailmanager.model.entity.MarketingFee;
import com.vince.retailmanager.model.entity.PercentageFee;
import com.vince.retailmanager.model.entity.Royalty;
import com.vince.retailmanager.repository.AccessTokensRepository;
import com.vince.retailmanager.repository.CompanyRepository;
import com.vince.retailmanager.repository.FranchiseeRepository;
import com.vince.retailmanager.repository.FranchisorRepository;
import com.vince.retailmanager.repository.PercentageFeeRepository;
import com.vince.retailmanager.web.controller.utils.ControllerUtils;
import com.vince.retailmanager.web.exception.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FranchiseServiceImpl implements FranchiseService {

  private UserService userService;
  private FinancialService financialService;

  private AccessTokensRepository accessTokensRepository;
  private CompanyRepository companyRepository;
  private FranchisorRepository franchisorRepository;
  private FranchiseeRepository franchiseeRepository;
  private PercentageFeeRepository percentageFeeRepository;

  private Validator validator;

  @Autowired
  public FranchiseServiceImpl(
      UserService userService,
      FinancialService financialService,

      AccessTokensRepository accessTokensRepository,
      CompanyRepository companyRepository,
      FranchisorRepository franchisorRepository,
      FranchiseeRepository franchiseeRepository,
      PercentageFeeRepository percentageFeeRepository,
      Validator validator
  ) {
    this.userService = userService;
    this.financialService = financialService;

    this.accessTokensRepository = accessTokensRepository;
    this.companyRepository = companyRepository;
    this.franchisorRepository = franchisorRepository;
    this.franchiseeRepository = franchiseeRepository;
    this.percentageFeeRepository = percentageFeeRepository;
    this.validator = validator;
  }


  @Override
  @Transactional
  public Company saveCompany(Company company) {
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

  @Override
  @Transactional(readOnly = true)
  public Franchisor findFranchisorById(int id) throws EntityNotFoundException {
    Franchisor franchisor = franchisorRepository.findById(id).orElse(null);
    if (franchisor == null) {
      throw new EntityNotFoundException(Franchisor.class, "id", String.valueOf(id));
    }
    return franchisor;
  }

  @Override
  @Transactional
  public Franchisor saveFranchisor(Franchisor franchisor) {
    return franchisorRepository.save(franchisor);
  }

  @Override
  @Transactional
  public void saveFranchisee(Franchisee franchisee) {
    franchiseeRepository.save(franchisee);
  }

  @Override
  @Transactional(readOnly = true)
  public List<Franchisor> findAllFranchisors() throws DataAccessException {
    return franchisorRepository.findAll();
  }


  @Override
  @Transactional(readOnly = true)
  public Franchisee findFranchiseeById(int id) throws EntityNotFoundException {
    Franchisee franchisee = franchiseeRepository.findById(id).orElse(null);
    if (franchisee == null) {
      throw new EntityNotFoundException(Franchisee.class, "id", String.valueOf(id));
    }
    return franchisee;
  }


  @Override
  public void disableFranchisor(Franchisor franchisor) throws InvalidOperationException {
    Set<Franchisee> franchisees = franchisor.getFranchisees();
    if (!franchisees.isEmpty()) {
      throwObjectStateException(franchisor, franchisees);
      return;
    }
    disableCompany(franchisor);
  }

  private void throwObjectStateException(Franchisor franchisor, Set<Franchisee> franchisees) {
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
        franchisorClassName,
        invalidValues
    );
  }


  @Override
  @Transactional
  public void disableCompany(Company company) {
    //check if there are any franchisees dependendent on franchisor
    //remove all access tokens associated with company /// but throw exception in constraint validator
    company.setEnabled(false);
    companyRepository.save(company);
  }


  @Override
  @Transactional
  public Royalty saveRoyalty(Royalty royalty) {
    return percentageFeeRepository.save(royalty);
  }

  @Override
  @Transactional
  public MarketingFee saveMarketingFee(MarketingFee marketingFee) {
    return percentageFeeRepository.save(marketingFee);
  }

  @Override
  @Transactional
  public PercentageFee savePercentageFee(PercentageFee percentageFee) {
    ControllerUtils.validate(validator, percentageFee);
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
  public List<PercentageFee> getPercentageFees(Franchisor franchisor) {
    if (franchisor == null) {
      return Collections.emptyList();
    }

    return percentageFeeRepository.findAllByFranchisorId(franchisor.getId());
  }
}

