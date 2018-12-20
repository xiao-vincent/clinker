package com.vince.retailmanager.security;

import org.springframework.stereotype.Component;

@Component
public class Roles {

  public final String FRANCHISOR = "ROLE_FRANCHISOR";
  public final String FRANCHISEE = "ROLE_FRANCHISEE";
  public final String ADMIN = "ROLE_ADMIN";
}
