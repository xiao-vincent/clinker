package com.vince.retailmanager.utils;

import java.text.DecimalFormat;

public class StringUtils {

	public static String singlePlural(int count, String word) {
		return count == 1 ? word : word + "s";
	}

	private static DecimalFormat df = new DecimalFormat("#,##0.00");

	public static String format(Object n) {
		return df.format(n);
	}


}
