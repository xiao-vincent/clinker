package com.vince.retailmanager.service;

import com.vince.retailmanager.model.entity.financials.IncomeStatement;
import com.vince.retailmanager.exception.EntityNotFoundException;

public interface FinancialService {

  IncomeStatement findIncomeStatementById(int id) throws EntityNotFoundException;

  IncomeStatement saveIncomeStatement(IncomeStatement incomeStatement);


}
