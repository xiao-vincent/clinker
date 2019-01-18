package com.vince.retailmanager.repository;

import com.vince.retailmanager.model.entity.authorization.AccessToken;
import com.vince.retailmanager.model.entity.companies.Company;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccessTokensRepository extends JpaRepository<AccessToken, Integer> {

  Optional<AccessToken> findByCompanyId(Integer id);

  Optional<AccessToken> findByUserUsernameAndCompanyId(String username, int companyId);

  Optional<AccessToken> findFirstByUserUsernameAndCompanyIn(String username,
      Set<Company> companies);

  void deleteAccessTokensByCompany_Id(Integer id);

  void deleteAccessTokensByCompany(Company company);

}
