package com.warren.contact.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

	public static String replaceBlank(String str) {
		String dest = "";
		if (str != null) {
			Pattern p = Pattern.compile("\\s*|\t|\r|\n");
			Matcher m = p.matcher(str);
			dest = m.replaceAll("");
		}
		return dest;
	}
	
	public static boolean isEmpty(String str) {
		if(str==null || str.trim().equals("")||str.equals("null")) {
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean equals(String str1,String str2) {
		if(str1==null&&str2==null) {
			return true;
		} 
		if(str1!=null&&str1.equals(str2)) {
			return true;
		} else {
			return false;
		}
	}

}
