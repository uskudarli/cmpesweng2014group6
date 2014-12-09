package com.android.dutluk;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * Class which has Utility methods
 * 
 */
public class Utility {
	
	public static String userName = "" ;
	public static String [] userList;
	public static String [] tagList;
	public static String [] locationList;
	
	public static final String SERVER_NAME = "http://titan.cmpe.boun.edu.tr:8085/dutluk_android_api/";
	
	
	private static Pattern pattern;
	private static Matcher matcher;
	//public static Map<Integer,ArrayList<Double>> multiMap = new HashMap<Integer,ArrayList<Double>>();
	
	//Email Pattern
	private static final String EMAIL_PATTERN = 
			"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	
	/**
	 * Validate Email with regular expression
	 * 
	 * @param email
	 * @return true for Valid Email and false for Invalid Email
	 */
	public static boolean validateEmail(String email) {
		pattern = Pattern.compile(EMAIL_PATTERN);
		matcher = pattern.matcher(email);
		return matcher.matches();
 
	}
	/**
	 * Checks for Null String object
	 * 
	 * @param txt
	 * @return true for not null and false for null String object
	 */
	public static boolean isNotNull(String txt){
		return txt!=null && txt.trim().length()>0 ? true: false;
	}
}
