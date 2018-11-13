package com.vince.retailmanager.web;

import com.vince.retailmanager.entity.User;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;

public class CustomMethodSecurityExpressionRoot extends SecurityExpressionRoot implements MethodSecurityExpressionOperations {

	private Object filterObject;
	private Object returnObject;

	public CustomMethodSecurityExpressionRoot(Authentication authentication) {
		super(authentication);
	}

	//
//	public boolean isMember(Long OrganizationId) {
//		final User user = ((MyUserPrincipal) this.getPrincipal()).getUser();
//		return user.getOrganization().getId().longValue() == OrganizationId.longValue();
//	}

	//
	public boolean isLoggedIn(User user) {
		return isLoggedIn(user.getUsername());
	}

	public boolean isLoggedIn(String username) {
		return authentication.getName().equals(username);
	}

	@Override

	public Object getFilterObject() {
		return this.filterObject;
	}

	@Override
	public void setFilterObject(Object obj) {
		this.filterObject = obj;
	}

	@Override
	public Object getReturnObject() {
		return this.returnObject;
	}

	@Override
	public void setReturnObject(Object obj) {
		this.returnObject = obj;
	}

	@Override
	public Object getThis() {
		return this;
	}
}
