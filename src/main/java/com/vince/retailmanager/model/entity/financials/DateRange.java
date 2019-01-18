package com.vince.retailmanager.model.entity.financials;

import java.time.LocalDate;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DateRange {

  private LocalDate startDate;
  private LocalDate endDate;

  public DateRange(LocalDate startDate, LocalDate endDate) {
    this.startDate = startDate;
    this.endDate = endDate;
  }
}