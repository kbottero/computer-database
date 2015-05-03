package com.excilys.cdb.controller;

import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

/**
 * Automatically transform empty string into null
 */
@ControllerAdvice //Permit to support all RequestMapping
public class TrimController {
    @InitBinder
    public void initBinder(WebDataBinder binder) {
    	//Initialising StringTrimmerEditor with true permit to change empty string to null value (needed for dates control)
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }
}