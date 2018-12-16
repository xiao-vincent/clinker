package com.vince.retailmanager;

import com.vince.retailmanager.entity.DistributionType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class DistributionTypeConverter implements Converter<String, DistributionType> {

	@Override
	public DistributionType convert(String type) {
		return DistributionType.fromString(type);
	}
}
