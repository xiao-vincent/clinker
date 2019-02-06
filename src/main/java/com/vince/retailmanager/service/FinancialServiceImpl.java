package com.vince.retailmanager.service;

import com.vince.retailmanager.exception.EntityNotFoundException;
import com.vince.retailmanager.model.entity.financials.IncomeStatement;
import com.vince.retailmanager.repository.IncomeStatementRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FinancialServiceImpl implements FinancialService {

  private IncomeStatementRepository incomeStatementRepository;

  public FinancialServiceImpl(
      IncomeStatementRepository incomeStatemetRepository
  ) {
    this.incomeStatementRepository = incomeStatementRepository;
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
