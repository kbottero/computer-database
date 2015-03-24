/**
 * 
 */
package com.excilys.cdb.validation;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Kevin Bottero
 *
 */
public class TestValidatorDate {

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link com.excilys.cdb.validation.ValidatorDate#validateDate(java.lang.String)}.
	 */
	@Test
	public void testValidateDate() {
		HashMap<String,Boolean> testList = new HashMap<String,Boolean>();
		testList.put("2000-01-01",true);
		testList.put("2000-01-32",false);
		testList.put("2000-14-01",false);
		testList.put("9999-01-01",false);
		testList.put("2000-02-29",true);
		testList.put("2004-02-29",true);
		testList.put("1900-02-29",false);
		testList.put("2000-02-30",false);
		testList.put("2000-02-31",false);
		testList.put("2000-04-31",false);
		testList.put("2000-06-31",false);
		testList.put("2000-09-31",false);
		testList.put("2000-11-31",false);
		testList.put("2000-01-31",true);
		testList.put("2000-03-31",true);
		testList.put("2000-05-31",true);
		testList.put("2000-07-31",true);
		testList.put("2000-08-31",true);
		testList.put("2000-10-31",true);
		testList.put("2000-12-31",true);
		
		for (Map.Entry<String,Boolean> entry : testList.entrySet()) {
			assertEquals(entry.getKey() + " => " + entry.getValue(),ValidatorDate.check(entry.getKey()),entry.getValue());
		}
		
	}

}
