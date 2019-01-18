package com.vince.retailmanager.security;

public enum RoleType {
  USER("USER"),
  ADMIN("ADMIN");

  RoleType(String val) {
    if (!this.name().equals(val)) {
      throw new IllegalArgumentException("Incorrect use of RoleType");
    }
  }

}
