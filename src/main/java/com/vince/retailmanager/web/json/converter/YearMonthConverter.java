package com.vince.retailmanager.web.json.converter;

import com.vince.retailmanager.web.constants.Date;
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
    YearMonth yearMonth = YearMonth
        .parse(text, DateTimeFormatter.ofPattern(Date.YEAR_MONTH_FORMAT));
    return yearMonth.atEndOfMonth();
  }
}
