package com.vince.retailmanager.service;

import com.vince.retailmanager.exception.InvalidOperationException;
import com.vince.retailmanager.model.entity.authorization.AccessToken;
import com.vince.retailmanager.model.entity.authorization.Role;
import com.vince.retailmanager.model.entity.authorization.User;
import com.vince.retailmanager.model.entity.companies.Company;
import com.vince.retailmanager.repository.AccessTokensRepository;
import com.vince.retailmanager.repository.UserRepository;
import com.vince.retailmanager.utils.ValidatorUtils;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

  @Autowired
  private UserRepository userRepository;
  @Autowired
  private AccessTokensRepository accessTokensRepository;
  @Autowired
  private ValidatorUtils validatorUtils;
  @Autowired
  private PasswordEncoder passwordEncoder;

  @Override
  @Transactional
  public void saveUser(User user) {
    if (user.getRoles() == null || user.getRoles().isEmpty()) {
      throw new InvalidOperationException("User must have at least one role set");
    }
    for (Role role : user.getRoles()) {
      if (!role.getName().startsWith("ROLE_")) {
        role.setName("ROLE_" + role.getName());
      }
      if (role.getUser() == null) {
        role.setUser(user);
      }
    }
    user.setPassword(passwordEncoder.encode(user.getPassword()));

    validatorUtils.validate(user);
    userRepository.save(user);
  }

  @Override
  public User findUser(String username) {
    return userRepository.findByUsername(username).orElse(null);
  }

  @Override
  @Transactional
  public AccessToken addAccessToken(String username, Company company) {
    User user = findUser(username);
    AccessToken accessToken = AccessToken.builder()
        .user(user)
        .company(company)
        .build();
    accessTokensRepository.save(accessToken);
    return accessToken;
  }


  @Override
  public AccessToken findAccessToken(String username, Set<Company> companies) {
    return accessTokensRepository.findFirstByUserUsernameAndCompanyIn(username, companies)
        .orElse(null);
  }
}

