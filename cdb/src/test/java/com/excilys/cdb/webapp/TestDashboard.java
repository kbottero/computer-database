package com.excilys.cdb.webapp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.excilys.cdb.dto.ComputerDTO;
import com.excilys.cdb.mapper.ComputerMapper;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.persistence.TransactionFactory;

public class TestDashboard {

	private WebDriver driver;
	private StringBuffer verificationErrors = new StringBuffer();
	
	private final static String baseUrl = "http://localhost:8080/cdb/dashboard";
	
	private static Connection connection;
	private static Statement statement;

	@BeforeClass
	public static void setUpDB() throws IOException, SQLException {
		System.setProperty("env", "TEST");
	}
	
	@Before 
	public void setUp() throws Exception {
		//Data-base
		BufferedReader in = new BufferedReader(new InputStreamReader(TransactionFactory.class.getClassLoader().getResourceAsStream("db-test.properties")));
		Properties properties = new Properties();
		properties.load(in);
		in.close();
		connection = DriverManager.getConnection(
				properties.getProperty("url")+properties.getProperty("database"),
				properties);
		statement = connection.createStatement();
		
		in = new BufferedReader(new FileReader("src/test/resources/schema.sql"));
		String line = in.readLine();
		while (line != null) {
			statement.execute(line);
			line = in.readLine();
		}
		in.close();
		//Test
		driver = new FirefoxDriver(); 
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS); 
	} 

	@After 
	public void tearDown() throws Exception { 
		if (statement != null) {
			statement.close();
		}
		if(connection != null) {
			connection.close();
		}
		
		driver.quit(); 
		String verificationErrorString = verificationErrors.toString(); 
		if (!"".equals(verificationErrorString)) { 
			fail(verificationErrorString); 
		} 
	}
	
	@Test 
	public void testDashboard() throws Exception {
		ArrayList<Computer> listComputer = new ArrayList<Computer>();
		ArrayList<Company> listCompany = new ArrayList<Company>();
		initDBWithBasicInfo(listComputer, listCompany);
		driver.get(baseUrl);
		List<ComputerDTO> listComputerDTO = ComputerMapper.INSTANCE.toDTOList(listComputer);
		for (int i =0; i<listComputerDTO.size() && i < 10; ++i) {
			WebElement query = driver.findElement(By.id("computer_"+listComputerDTO.get(i).getId()));
			assertNotNull(query);
			System.out.println(query.getText());
			assertEquals(query.getText(), listComputerDTO.get(i).getName());
		}
	}
	
	private void initDBWithBasicInfo (ArrayList<Computer> listComputer, ArrayList<Company> listCompany) throws Exception {
		ArrayList<String> list = new ArrayList<String>();

		list.add("insert into company (id,name) values (  1,'Apple Inc.');");
		list.add("insert into company (id,name) values (  2,'Thinking Machines');");
		list.add("insert into company (id,name) values (  3,'RCA');");
		list.add("insert into company (id,name) values (  4,'Netronics');");
		list.add("insert into company (id,name) values (  5,'Tandy Corporation');");
		list.add("insert into company (id,name) values (  6,'Commodore International');");
		
		list.add("insert into computer (id,name,introduced,discontinued,company_id) values (  1,'Computer1',null,null,null);");
		list.add("insert into computer (id,name,introduced,discontinued,company_id) values (  2,'Computer 2','1992-01-01',null,null);");
		list.add("insert into computer (id,name,introduced,discontinued,company_id) values (  3,'Computer thr33','1992-01-01','1992-01-01',null);");
		list.add("insert into computer (id,name,introduced,discontinued,company_id) values (  4,'Computer 4','1991-01-01','1992-01-01',2);");
		list.add("insert into computer (id,name,introduced,discontinued,company_id) values (  5,'Computer 5',null,'1992-01-01',3);");
		list.add("insert into computer (id,name,introduced,discontinued,company_id) values (  6,'Computer 6','2006-01-10',null,4);");
		list.add("insert into computer (id,name,introduced,discontinued,company_id) values (  7,'Computer 7',null,null,1);");
		list.add("insert into computer (id,name,introduced,discontinued,company_id) values (  8,'Computer 8',null,'2006-01-10',null);");
		
		for (String request : list) {
				statement.execute(request);
		}
		
		if (listCompany != null && listComputer != null) {
			listCompany.add(new Company(1l,"Apple Inc."));
			listCompany.add(new Company(2l,"Thinking Machines"));
			listCompany.add(new Company(3l,"RCA"));
			listCompany.add(new Company(4l,"Netronics"));
			listCompany.add(new Company(5l,"Tandy Corporation"));
			listCompany.add(new Company(6l,"Commodore International"));
			
			listComputer.add(new Computer (1l,"Computer1",null,null,null));
			listComputer.add(new Computer (2l,"Computer 2",LocalDateTime.parse("1992-01-01T00:00:00",DateTimeFormatter.ISO_DATE_TIME),null,null));
			listComputer.add(new Computer (3l,"Computer thr33",LocalDateTime.parse("1992-01-01T00:00:00",DateTimeFormatter.ISO_DATE_TIME),LocalDateTime.parse("1992-01-01T00:00:00",DateTimeFormatter.ISO_DATE_TIME),null));
			listComputer.add(new Computer (4l,"Computer 4",LocalDateTime.parse("1991-01-01T00:00:00",DateTimeFormatter.ISO_DATE_TIME),LocalDateTime.parse("1992-01-01T00:00:00",DateTimeFormatter.ISO_DATE_TIME),listCompany.get(2)));
			listComputer.add(new Computer (5l,"Computer 5",null,LocalDateTime.parse("1992-01-01T00:00:00",DateTimeFormatter.ISO_DATE_TIME),listCompany.get(3)));
			listComputer.add(new Computer (6l,"Computer 6",LocalDateTime.parse("2006-01-10T00:00:00",DateTimeFormatter.ISO_DATE_TIME),null,listCompany.get(4)));
			listComputer.add(new Computer (7l,"Computer 7",null,null,listCompany.get(1)));
			listComputer.add(new Computer (8l,"Computer 8",null,LocalDateTime.parse("2006-01-10T00:00:00",DateTimeFormatter.ISO_DATE_TIME),null));
		}
	}

}
