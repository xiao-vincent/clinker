package com.vince.retailmanager.service;

import com.vince.retailmanager.model.entity.authorization.AccessToken;
import com.vince.retailmanager.model.entity.authorization.User;
import com.vince.retailmanager.model.entity.companies.Company;
import java.util.Set;
import org.springframework.transaction.annotation.Transactional;

public interface UserService {

  User saveUser(User user);

  User findUser(String username);

  @Transactional
  AccessToken addAccessToken(String username, Company company);

  AccessToken findAccessToken(String username, Set<Company> companies);
}
