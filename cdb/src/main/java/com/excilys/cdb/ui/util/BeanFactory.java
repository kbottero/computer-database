package com.excilys.cdb.ui.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.excilys.cdb.service.CompaniesService;
import com.excilys.cdb.service.ComputersService;

public enum BeanFactory {
	INSTANCE;

	private ApplicationContext applicationContext;
	
	BeanFactory() {
		applicationContext = new ClassPathXmlApplicationContext("spring-config.xml");
	}
	
	public ComputersService getServiceComputer() {
		return (ComputersService)applicationContext.getBean("serviceComputer");
	}

	public CompaniesService getServiceCompany() {
		return (CompaniesService)applicationContext.getBean("serviceCompany");
	}
	
}
