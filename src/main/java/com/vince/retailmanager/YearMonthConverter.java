package com.vince.retailmanager;

import com.vince.retailmanager.model.constants.Date;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import org.springframework.core.convert.converter.Converter;

public class YearMonthConverter implements Converter<String, LocalDate> {

  public static LocalDate createEndOfMonthDate(int year, int month) {
    return YearMonth.of(year, month).atEndOfMonth();
  }

  @Override
  public LocalDate convert(String text) {
    YearMonth yearMonth = YearMonth.parse(text, DateTimeFormatter.ofPattern(Date.DATE_FORMAT));
    return yearMonth.atEndOfMonth();
  }
}
