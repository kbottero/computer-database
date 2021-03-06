package com.excilys.cdb.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidatorLocalDateTime {
	/**
	 * Date validator
	 * @param date
	 * @return true if the String correspond to an existing date, false otherwise.
	 */
	public static boolean check (final String date){
		if (date == null) {
			return false;
		}

		Pattern pattern = Pattern.compile("((19|20)\\d\\d)[-](0[1-9]|1[012])[-](0[1-9]|[12][0-9]|3[01])");
		Matcher matcher = pattern.matcher(date);

		if (matcher.matches()) {
			matcher.reset();
			if (matcher.find()) {
				int year = Integer.parseInt(matcher.group(1));
				String month = matcher.group(3);
				String day = matcher.group(4);

				if (day.equals("31") && 
						(month.equals("04") || month .equals("06") || month.equals("09") || month.equals("11"))) {
					return false;
				} else if (month.equals("02")) {
					if((year % 4==0) && ((year % 100 != 0) || (year % 400 == 0))) {
						if(day.equals("30") || day.equals("31")){
							return false;
						} else {
							return true;
						}
					} else {
						if (day.equals("29")||day.equals("30")||day.equals("31")) {
							return false;
						} else {
							return true;
						}
					}
				} else {				 
					return true;				 
				}
			} else {
				return false;
			}		  
		} else {
			return false;
		}			    
	}
}

