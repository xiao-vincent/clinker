package com.vince.retailmanager.utils;

import java.time.LocalDate;

public class DateUtils {
	public static boolean checkBetween(LocalDate dateToCheck, LocalDate startDate, LocalDate endDate) {
		return dateToCheck.compareTo(startDate) >= 0 && dateToCheck.compareTo(endDate) <= 0;
	}
}
