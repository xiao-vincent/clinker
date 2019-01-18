package com.vince.retailmanager.web.json.converter;

import com.vince.retailmanager.model.entity.transactions.DistributionType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class DistributionTypeConverter implements Converter<String, DistributionType> {

  @Override
  public DistributionType convert(String type) {
    return DistributionType.fromString(type);
  }
}
