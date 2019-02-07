package com.vince.retailmanager.service;

import com.vince.retailmanager.exception.EntityNotFoundException;
import com.vince.retailmanager.model.entity.companies.Company;
import com.vince.retailmanager.model.entity.companies.Franchisor;
import com.vince.retailmanager.model.entity.fees.FeeType;
import com.vince.retailmanager.model.entity.fees.PercentageFee;
import com.vince.retailmanager.model.entity.financials.IncomeStatement;
import java.util.List;
import java.util.function.Function;
import javax.validation.constraints.NotNull;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;

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

  /**
   * Saves the percentageFee entity
   */
  @NotNull
  PercentageFee savePercentageFee(PercentageFee percentageFee);

  /**
   * Creates monthly franchise fees based on the franchisee's incomeStatement charged by the
   * franchisor
   *
   * @param incomeStatement the franchisee's incomeStatement used to calculate the fees
   * @param percentageFeeSuppliers the percentageFee constructors
   * @param <R> percentageFee subclasses (ex. royalty, marketingFee)
   * @return a list of created percentageFees charged to the franchisee
   */
  @Transactional
  <R extends PercentageFee> List<PercentageFee> createMonthlyFranchiseFees(
      IncomeStatement incomeStatement,
      Function<IncomeStatement, PercentageFee>... percentageFeeSuppliers);

  /**
   * Gets a list of outstanding percentageFees charged by the franchisor
   *
   * @param type the percentageFee type (ex. royalty, marketingFee)
   * @return a list of percentageFees charged to all franchisees
   */
  List<PercentageFee> getPercentageFees(@NotNull Franchisor franchisor, FeeType type);

}
