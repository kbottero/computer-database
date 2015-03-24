package com.excilys.cdb.validation;

import com.excilys.cdb.model.Company;

public class ValidatorCompany {
	
	public static boolean check(Company instance) {
		if ((instance == null)  || 
			(instance.getId() == null) ||
			(instance.getName() == null) ||
			(instance.getName().isEmpty())){
			return false;
		} 
		return true;
	}
}
