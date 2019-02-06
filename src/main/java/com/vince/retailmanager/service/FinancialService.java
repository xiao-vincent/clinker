package com.vince.retailmanager.service;

import com.vince.retailmanager.exception.EntityNotFoundException;
import com.vince.retailmanager.model.entity.financials.IncomeStatement;
import javax.validation.constraints.NotNull;
import org.springframework.lang.Nullable;

public interface FinancialService {

  /**
   * Finds an incomeStatement by ID.
   *
   * @param id the id of the incomeStatement
   * @throws EntityNotFoundException if incomeStatement is not found
   */
  @Nullable
  IncomeStatement findIncomeStatementById(int id) throws EntityNotFoundException;

  /**
   * Saves the incomeStatement entity.
   */
  @NotNull
  IncomeStatement saveIncomeStatement(@NotNull IncomeStatement incomeStatement);


}
