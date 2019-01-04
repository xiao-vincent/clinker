package com.vince.retailmanager;

import com.vince.retailmanager.model.entity.PercentageFee;
import com.vince.retailmanager.model.entity.PercentageFee.FeeType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class FeeTypeConverter implements Converter<String, PercentageFee.FeeType> {

  @Override
  public PercentageFee.FeeType convert(String type) {
    return FeeType.fromString(type);
  }
}
