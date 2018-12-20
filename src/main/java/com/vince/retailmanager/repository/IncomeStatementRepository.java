package com.vince.retailmanager.repository;

import com.vince.retailmanager.model.entity.IncomeStatement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IncomeStatementRepository extends JpaRepository<IncomeStatement, Integer> {

}
