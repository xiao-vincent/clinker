package com.vince.retailmanager.service;

import com.vince.retailmanager.entity.AccessToken;
import com.vince.retailmanager.entity.User;

public interface UserService {
	void saveUser(User user) throws Exception;

	User findUser(String username);

	AccessToken findAccessToken(int companyId);
}
