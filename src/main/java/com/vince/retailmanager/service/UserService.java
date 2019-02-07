package com.vince.retailmanager.service;

import com.vince.retailmanager.model.entity.authorization.AccessToken;
import com.vince.retailmanager.model.entity.authorization.User;
import com.vince.retailmanager.model.entity.companies.Company;
import java.util.Set;
import javax.validation.constraints.NotNull;
import org.springframework.lang.Nullable;

public interface UserService {

  /**
   * Saves the user entity
   */
  @NotNull
  User saveUser(User user);

  /**
   * Finds a user with the associated username
   *
   * @param username the username associated with the user
   */
  @Nullable
  User findUser(String username);

  /**
   * Saves an accessToken entity with the param values.
   *
   * @param username the username associated with an existing user
   * @param company the company associated with an existing company
   * @return the accessToken saved to the database
   */
  AccessToken addAccessToken(String username, Company company);

  /**
   * Finds the first accessToken associated with the username and company in the set of companies
   *
   * @return the accessToken associated with the username and company in the set of companies
   */
  @Nullable
  AccessToken findFirstAccessToken(String username, Set<Company> companies);
}
