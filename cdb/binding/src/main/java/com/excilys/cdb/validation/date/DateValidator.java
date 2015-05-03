package com.excilys.cdb.validation.date;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

public class DateValidator implements ConstraintValidator<DateAnnotation, String> {
	
	@Autowired
	private MessageSource messageSource;

	@Override
	public void initialize(DateAnnotation constraintAnnotation) {
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (value == null) {
			return true;
		}
		String regex = messageSource.getMessage(
				"date.pattern", null,
				LocaleContextHolder.getLocale());
		return value.matches(regex);
	}
}