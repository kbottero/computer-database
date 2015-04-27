package com.excilys.cdb.validation.date;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint(validatedBy = ValidatorDate.class)
@Target(value = ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DateAnnotation {
	String message() default "{date.invalidFormat}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}