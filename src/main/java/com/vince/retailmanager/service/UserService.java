package com.vince.retailmanager.service;

import com.vince.retailmanager.model.entity.AccessToken;
import com.vince.retailmanager.model.entity.User;

public interface UserService {

  void saveUser(User user) throws Exception;

  User findUser(String username);

  AccessToken findAccessToken(int companyId);

  AccessToken findAccessToken(String username, int companyId);
}
