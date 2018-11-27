package com.vince.retailmanager.utils;

public class StringUtils {

	public static String singlePlural(int count, String word) {
		return count == 1 ? word : word + "s";
	}
}
