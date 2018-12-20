package com.vince.retailmanager.model;

import com.vince.retailmanager.exception.InvalidEnumArgumentException;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum DistributionType {
  RECEIVED(),
  SENT();

  private static final Map<String, DistributionType> NAME_MAP = Stream.of(values())
      .collect(Collectors.toMap(DistributionType::toString, Function.identity()));

  public static DistributionType fromString(String name) {
    name = name.toLowerCase();
    DistributionType distributionType = NAME_MAP.get(name);
    if (distributionType == null) {
      throw new InvalidEnumArgumentException(String.format(
          "'%s' has no corresponding value. Accepted values: %s",
          name,
          Arrays.asList(values())
      )
      );
    }
    return distributionType;
  }

  @Override
  public String toString() {
    return this.name().toLowerCase();
  }

}
