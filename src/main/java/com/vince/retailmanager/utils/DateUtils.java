package com.vince.retailmanager.utils;

import com.vince.retailmanager.model.DateRange;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Set;

public class DateUtils {

  public static boolean checkBetween(LocalDate dateToCheck,
      DateRange dateRange) {
    LocalDate startDate = dateRange.getStartDate();
    LocalDate endDate = dateRange.getEndDate();
    return dateToCheck.compareTo(startDate) >= 0 && dateToCheck.compareTo(endDate) <= 0;
  }

  public static Set<YearMonth> getInclusiveRange(DateRange dateRange) {
    LocalDate startDate = dateRange.getStartDate();
    LocalDate endDate = dateRange.getEndDate();
    int initCapacity = ((int) ChronoUnit.MONTHS.between(startDate, endDate)) + 1;
    Set<YearMonth> yearMonths = new HashSet<>(initCapacity);
    YearMonth yearMonth = YearMonth.from(startDate);
    YearMonth end = YearMonth.from(endDate);
    while (yearMonth.compareTo(end) <= 0) {
      yearMonths.add(yearMonth);
      yearMonth = yearMonth.plusMonths(1);
    }
    return yearMonths;
  }

}
