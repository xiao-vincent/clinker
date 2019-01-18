package com.vince.retailmanager.web.json.converter;

import com.vince.retailmanager.model.entity.fees.FeeType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class FeeTypeConverter implements Converter<String, FeeType> {

  @Override
  public FeeType convert(String type) {
    return FeeType.fromString(type);
  }
}
