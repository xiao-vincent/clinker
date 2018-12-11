package com.vince.retailmanager.service;

import com.vince.retailmanager.entity.IncomeStatement;
import com.vince.retailmanager.repository.*;
import com.vince.retailmanager.web.exception.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FinancialServiceImpl implements FinancialService {
	private UserService userService;
	private AccessTokensRepository accessTokensRepository;
	private CompanyRepository companyRepository;
	private FranchisorRepository franchisorRepository;
	private FranchiseeRepository franchiseeRepository;
	private IncomeStatementRepository incomeStatementRepository;
	private PercentageFeeRepository percentageFeeRepository;

	@Autowired
	public FinancialServiceImpl(
		 UserService userService,
		 AccessTokensRepository accessTokensRepository,
		 CompanyRepository companyRepository,
		 FranchisorRepository franchisorRepository,
		 FranchiseeRepository franchiseeRepository,
		 IncomeStatementRepository incomeStatementRepository,
		 PercentageFeeRepository percentageFeeRepository
	) {
		this.userService = userService;
		this.accessTokensRepository = accessTokensRepository;
		this.companyRepository = companyRepository;
		this.franchisorRepository = franchisorRepository;
		this.franchiseeRepository = franchiseeRepository;
		this.incomeStatementRepository = incomeStatementRepository;
		this.percentageFeeRepository = percentageFeeRepository;
	}

	@Override
	@Transactional
	public IncomeStatement findIncomeStatementById(int id) throws EntityNotFoundException {
		IncomeStatement incomeStatement = incomeStatementRepository.findById(id).orElse(null);
		if (incomeStatement == null) {
			throw new EntityNotFoundException(IncomeStatement.class, "id", String.valueOf(id));
		}
		return incomeStatement;
	}

	@Override
	@Transactional
	public IncomeStatement saveIncomeStatement(IncomeStatement incomeStatement) {
		return incomeStatementRepository.save(incomeStatement);
	}
}
