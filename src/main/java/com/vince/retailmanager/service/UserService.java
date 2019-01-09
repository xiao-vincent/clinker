package com.vince.retailmanager.service;

import com.vince.retailmanager.model.entity.AccessToken;
import com.vince.retailmanager.model.entity.Company;
import com.vince.retailmanager.model.entity.User;
import java.util.Set;
import org.springframework.transaction.annotation.Transactional;

public interface UserService {

  void saveUser(User user);

  User findUser(String username);


  @Transactional
  AccessToken addAccessToken(String username, Company company);

  AccessToken findAccessToken(String username, int companyId);

  AccessToken findAccessToken(String username, Set<Company> companies);
}
