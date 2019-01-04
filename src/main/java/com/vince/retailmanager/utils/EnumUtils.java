package com.vince.retailmanager.utils;

import com.vince.retailmanager.exception.InvalidEnumArgumentException;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EnumUtils {

  public static <T extends Enum<T>> T fromString(Map<String, T> nameMap, String name) {
    name = name.toLowerCase();
    T enumObject = nameMap.get(name);
    if (enumObject == null) {
      throw new InvalidEnumArgumentException(String.format(
          "'%s' has no corresponding value. Accepted values: %s",
          name,
          nameMap.values().stream().map(t -> t.toString().toLowerCase())
              .collect(Collectors.toList())
      )
      );
    }
    return enumObject;
  }

  public static <T extends Enum<T>> Map<String, T> createNameMap(Class<T> enumType) {
    return Stream.of(enumType.getEnumConstants())
        .collect(Collectors.toMap(t -> t.toString().toLowerCase(), Function.identity()));
  }
}
