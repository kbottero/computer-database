package com.excilys.cdb.validation;

import java.util.regex.Pattern;

public enum ValidatorDate {
	
	INSTANCE;
	/**
	* Validate date. (yyyy-MM-dd)
	*
	* @param inputString The input string
	* @return True, if successful
	*/
	public boolean validateDate(String inputString) {
		Pattern p = Pattern.compile("^\\d{4}[-]?\\d{1,2}[-]?\\d{1,2}");
		return p.matcher(inputString).matches();
	}
}

