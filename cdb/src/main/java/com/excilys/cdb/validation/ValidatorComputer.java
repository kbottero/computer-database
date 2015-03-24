package com.excilys.cdb.validation;

import com.excilys.cdb.model.Computer;

public class ValidatorComputer {

	public static boolean check(Computer instance) {
		
		if ((instance == null)  || 
			(instance.getId() == null) ||
			(instance.getName() == null) ||
			(instance.getName().isEmpty())	||
			(instance.getIntroductionDate().isAfter(instance.getDiscontinuedDate())) ||
			(instance.getConstructor() != null && ValidatorCompany.check(instance.getConstructor()))){
			return false;
		} 
		return true;
	}

}
