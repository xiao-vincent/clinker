package com.vince.retailmanager.repository;

import com.vince.retailmanager.model.entity.companies.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Integer> {

}
