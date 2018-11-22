package com.vince.retailmanager.repository;

import com.vince.retailmanager.entity.AccessToken;
import com.vince.retailmanager.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccessTokensRepository extends JpaRepository<AccessToken, Integer> {
	Optional<AccessToken> findByCompanyId(Integer id);

	Optional<AccessToken> findByUser_UsernameAndCompany_Id(String username, int companyId);

	void deleteAccessTokensByCompany_Id(Integer id);

	void deleteAccessTokensByCompany(Company company);

}
