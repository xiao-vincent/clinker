package com.vince.retailmanager.model;

import com.vince.retailmanager.utils.EnumUtils;
import java.util.Map;

public enum DistributionType {
  RECEIVED(),
  SENT();

  private static final Map<String, DistributionType> NAME_MAP = EnumUtils
      .createNameMap(DistributionType.class);

  public static DistributionType fromString(String name) {
    return EnumUtils.fromString(NAME_MAP, name);
  }

}
