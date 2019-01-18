package com.vince.retailmanager.repository;

import com.vince.retailmanager.model.entity.financials.IncomeStatement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IncomeStatementRepository extends JpaRepository<IncomeStatement, Integer> {

}
