package com.vince.retailmanager.model.entity.fees;

import com.vince.retailmanager.utils.EnumUtils;
import java.util.Map;

public enum FeeType {
  ROYALTY(Constants.ROYALTY),
  MARKETING(Constants.MARKETING);

  public static class Constants {

    public static final String ROYALTY = "ROYALTY";
    public static final String MARKETING = "MARKETING";
  }

  FeeType(String val) {
    // force equality between name of enum instance, and value of constant
    if (!this.name().equals(val)) {
      throw new IllegalArgumentException("Incorrect use of FeeType");
    }
  }

  public static final Map<String, FeeType> NAME_MAP = EnumUtils.createNameMap(FeeType.class);

  public static FeeType fromString(String name) {
    return EnumUtils.fromString(NAME_MAP, name);
  }

}
