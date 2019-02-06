package com.vince.retailmanager.service;

import com.vince.retailmanager.exception.EntityNotFoundException;
import com.vince.retailmanager.model.entity.companies.Company;
import com.vince.retailmanager.model.entity.companies.Franchisor;
import com.vince.retailmanager.model.entity.fees.FeeType;
import com.vince.retailmanager.model.entity.fees.PercentageFee;
import com.vince.retailmanager.model.entity.financials.IncomeStatement;
import java.util.List;
import javax.validation.constraints.NotNull;
import org.springframework.lang.Nullable;

public interface FranchiseService {

  /**
   * Saves the company entity.
   */
  @NotNull <R extends Company> R saveCompany(@NotNull R company);

  /**
   * Finds the company by ID.
   *
   * @throws EntityNotFoundException if company is not found
   */
  @Nullable
  <R extends Company> R findCompanyById(int id) throws EntityNotFoundException;


  /**
   * Disables the franchisor, marking the franchisor as inactive
   *
   * @param franchisor must not have active franchisees
   */
  void disable(@NotNull Franchisor franchisor);

  PercentageFee savePercentageFee(PercentageFee percentageFee);

  List<PercentageFee> createMonthlyFranchiseFees(IncomeStatement incomeStatement);

  List<PercentageFee> getPercentageFees(Franchisor franchisor, FeeType type);

}
