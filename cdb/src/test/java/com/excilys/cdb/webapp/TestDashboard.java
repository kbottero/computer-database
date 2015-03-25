package com.excilys.cdb.webapp;

import static org.junit.Assert.fail;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class TestDashboard {

	private WebDriver driver; 
	private String baseUrl;
	private boolean acceptNextAlert = true; 
	private StringBuffer verificationErrors = new StringBuffer(); 

	@Before 

	public void setUp() throws Exception { 
		// On instancie notre driver, et on configure notre temps d'attente 
		driver = new FirefoxDriver(); 
		baseUrl = "http://localhost:8080/cdb"; 
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS); 
	} 

	@Test 
	public void testSelenium() throws Exception { 
		// On se connecte au site 
		driver.get(baseUrl); 
	} 

	@After 
	public void tearDown() throws Exception { 
		driver.quit(); 
		String verificationErrorString = verificationErrors.toString(); 
		if (!"".equals(verificationErrorString)) { 
			fail(verificationErrorString); 
		} 
	}

	private boolean isElementPresent(By by) { 
		try { 
			driver.findElement(by); 
			return true; 
		} catch (NoSuchElementException e) { 
			return false; 
		} 
	} 

	private String closeAlertAndGetItsText() { 
		try { 
			Alert alert = driver.switchTo().alert(); 
			if (acceptNextAlert) {
				alert.accept(); 
			} else { 
				alert.dismiss(); 
			} 
			return alert.getText(); 
		} finally { 
			acceptNextAlert = true; 
		} 
	} 

}
