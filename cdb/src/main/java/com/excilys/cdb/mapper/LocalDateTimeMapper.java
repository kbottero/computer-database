package com.excilys.cdb.mapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
public class LocalDateTimeMapper {
	
	private final static String DATETIME_FORMAT_LABEL = "dateTime.format";
	private final static String DATE_FORMAT_LABEL = "date.format";
	private final static String DEFAULT_TIME = "date.defaultTime";

	@Autowired
	private MessageSource messageSource;
	
	public String toDTO (LocalDateTime date) {
		if (date == null) {
			return null;
		} 
		Locale locale = LocaleContextHolder.getLocale();
		String datePattern = messageSource.getMessage(DATE_FORMAT_LABEL, null, locale);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(datePattern);
		return date.format(formatter);
	}
	
	public LocalDateTime fromDTO (String date) {
		if (date == null) {
			return null;
		} 
		Locale locale = LocaleContextHolder.getLocale();
		String datePattern = messageSource.getMessage(DATETIME_FORMAT_LABEL, null, locale);
		String defaultTime = messageSource.getMessage(DEFAULT_TIME, null, locale);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(datePattern);
		return LocalDateTime.parse(date + defaultTime, formatter);
	}
}
