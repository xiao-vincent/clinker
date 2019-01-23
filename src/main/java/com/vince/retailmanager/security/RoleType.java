package com.vince.retailmanager.security;

public enum RoleType {
  USER(Constants.USER),
  ADMIN(Constants.ADMIN);

  RoleType(String val) {
    if (!this.name().equals(val)) {
      throw new IllegalArgumentException("Incorrect use of RoleType");
    }
  }

  public static class Constants {

    public static final String USER = "USER";
    public static final String ADMIN = "ADMIN";


  }

}
