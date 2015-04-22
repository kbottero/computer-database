package com.excilys.cdb.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.persistence.dao.impl.CompanyDao;
import com.excilys.cdb.persistence.util.DaoRequestParameter;
import com.excilys.cdb.persistence.util.DaoRequestParameter.Order;

/**
 * @author Kevin Bottero
 *
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/application-context-test.xml"})
public class TestCompanyDao {

	private Connection connection;
	private Statement statement;
	
	@Autowired
	private CompanyDao dao;

	@Before
	public void setUp() throws Exception {
		
		BufferedReader in = new BufferedReader(new InputStreamReader(TestCompanyDao.class.getClassLoader().getResourceAsStream("db-test.properties")));
		Properties properties = new Properties();
		properties.load(in);
		in.close();
		connection = DriverManager.getConnection(
				properties.getProperty("url"),properties.getProperty("username"),properties.getProperty("password"));
		statement = connection.createStatement();
		
		in = new BufferedReader(new FileReader("src/test/resources/schema.sql"));
		String line = in.readLine();
		while (line != null) {
			statement.execute(line);
			line = in.readLine();
		}
		in.close();
	}
	
	@After
	public void tearDown() throws Exception {
		if (statement != null) {
			statement.close();
		}
		if(connection != null) {
			connection.close();
		}
	}

	@Test
	public void getAllWithNoOrder() throws Exception {
		ArrayList<Computer> listComputer = new ArrayList<Computer>();
		ArrayList<Company> listCompany = new ArrayList<Company>();
		initDBWithBasicInfo(listComputer, listCompany);
		
		ArrayList<Company> result = (ArrayList<Company>) dao.getAll();
		
		assertTrue(	listCompany.size() == result.size());
		for (int i=0; i<listCompany.size() ;++i) {
			assertTrue(result.get(i).equals(listCompany.get(i)));
		}
	}
	
	@Test(expected = Exception.class)
	public void getAllOnEmptyDatabase() throws Exception {
		statement.execute("drop table if exists computer;");
		statement.execute("drop table if exists company;");
		dao.getAll();
	}
	
	@Test(expected = NullPointerException.class)
	public void getAllWithNullParam() throws Exception {
		ArrayList<Computer> listComputer = new ArrayList<Computer>();
		ArrayList<Company> listCompany = new ArrayList<Company>();
		initDBWithBasicInfo(listComputer, listCompany);
		dao.getAll(null);
	}
	
	@Test
	public void getAllWithOrderByName() throws Exception {
		ArrayList<Computer> listComputer = new ArrayList<Computer>();
		ArrayList<Company> listCompany = new ArrayList<Company>();
		initDBWithBasicInfo(listComputer, listCompany);
		LinkedHashMap<String,Order> list = new LinkedHashMap<>();
		list.put("name",Order.DESC);
		DaoRequestParameter param = new DaoRequestParameter (null,null,list,null,null);
		ArrayList<Company> result = (ArrayList<Company>) dao.getAll(param);
		
		ArrayList<String> nameList = new ArrayList<String>();
		for (int i=0; i<listCompany.size() ;++i) {
			nameList.add(listCompany.get(i).getName());
		}
		java.util.Collections.sort(nameList);
		java.util.Collections.reverse(nameList);
		
		assertTrue(	listCompany.size() == result.size());
		for (int i=0; i<listCompany.size() ;++i) {
			assertTrue(result.get(i).getName().equals(nameList.get(i)));
		}
	}
	
	@Test
	public void getAllWithOrderByNameAndId() throws Exception {
		ArrayList<Computer> listComputer = new ArrayList<Computer>();
		ArrayList<Company> listCompany = new ArrayList<Company>();
		initDBWithBasicInfo(listComputer, listCompany);
		LinkedHashMap<String,Order> list = new LinkedHashMap<>();
		list.put("name",Order.DESC);
		list.put("id",Order.ASC);
		DaoRequestParameter param = new DaoRequestParameter (null,null,list,null,null);
		ArrayList<Company> result = (ArrayList<Company>) dao.getAll(param);
		
		ArrayList<String> nameList = new ArrayList<String>();
		for (int i=0; i<listCompany.size() ;++i) {
			nameList.add(listCompany.get(i).getName());
		}
		java.util.Collections.sort(nameList);
		java.util.Collections.reverse(nameList);
		
		assertTrue(	listCompany.size() == result.size());
		for (int i=0; i<listCompany.size() ;++i) {
			assertTrue(result.get(i).getName().equals(nameList.get(i)));
		}
	}
	
	@Test(expected = NullPointerException.class)
	public void getSomeWithNullParameter() throws Exception {
		dao.getSome(null);
	}

	@Test(expected = Exception.class)
	public void getSomeWithRequestParameterAttributeNull() throws Exception {
		ArrayList<Company> list = (ArrayList<Company>) dao.getSome(new DaoRequestParameter(null, null, null, null, null));
		assertNotNull(list);
		assertNotNull(list.size());
	}
	
	@Test
	public void getSomeWithFilter() throws Exception {
		ArrayList<Computer> listComputer = new ArrayList<Computer>();
		ArrayList<Company> listCompany = new ArrayList<Company>();
		initDBWithBasicInfo(listComputer, listCompany);
		ArrayList<Company> list = (ArrayList<Company>) dao.getSome(new DaoRequestParameter(DaoRequestParameter.NameFiltering.POST, "T", null, 10l, null));
		assertEquals(list.size(),2);
	}
	
	@Test(expected = Exception.class)
	public void getSomeOnEmptyDatabase() throws Exception {
		statement.execute("drop table if exists computer;");
		statement.execute("drop table if exists company;");
		dao.getSome(new DaoRequestParameter(null, null, null, 10l, null));
	}
	
	@Test
	public void getSomeWithOrderByName() throws Exception {
		ArrayList<Computer> listComputer = new ArrayList<Computer>();
		ArrayList<Company> listCompany = new ArrayList<Company>();
		initDBWithBasicInfo(listComputer, listCompany);
		LinkedHashMap<String,Order> list = new LinkedHashMap<>();
		list.put("name",Order.DESC);
		DaoRequestParameter param = new DaoRequestParameter (null,null,list,4l,null);
		ArrayList<Company> result = (ArrayList<Company>) dao.getSome(param);
		
		ArrayList<String> nameList = new ArrayList<String>();
		for (int i=0; i<listCompany.size(); ++i) {
			nameList.add(listCompany.get(i).getName());
		}
		java.util.Collections.sort(nameList);
		java.util.Collections.reverse(nameList);
		
		assertTrue(4 <= result.size());
		for (int i=0; i<result.size() ;++i) {
			assertTrue(result.get(i).getName().equals(nameList.get(i)));
		}
	}
	
	@Test
	public void getSomeWithOrderByNameAndOffset() throws Exception {
		ArrayList<Computer> listComputer = new ArrayList<Computer>();
		ArrayList<Company> listCompany = new ArrayList<Company>();
		initDBWithBasicInfo(listComputer, listCompany);
		LinkedHashMap<String,Order> list = new LinkedHashMap<>();
		list.put("name",Order.DESC);
		DaoRequestParameter param = new DaoRequestParameter (null,null,list,4l,1l);
		ArrayList<Company> result = (ArrayList<Company>) dao.getSome(param);
		
		ArrayList<String> nameList = new ArrayList<String>();
		for (int i=0; i<listCompany.size() ;++i) {
			nameList.add(listCompany.get(i).getName());
		}
		java.util.Collections.sort(nameList);
		java.util.Collections.reverse(nameList);
		
		assertTrue(4 <= result.size());
		for (int i=0; i<result.size() ;++i) {
			assertTrue(result.get(i).getName().equals(nameList.get(i+1)));
		}
	}
	
	@Test
	public void getNb() throws Exception {
		ArrayList<Computer> listComputer = new ArrayList<Computer>();
		ArrayList<Company> listCompany = new ArrayList<Company>();
		initDBWithBasicInfo(listComputer, listCompany);
		assertTrue(dao.getNb() == listCompany.size());
	}
	
	@Test
	public void getNbOnEmptyTable() throws Exception {
		assertEquals(new Long (0l),dao.getNb());
	}

	@Test(expected = Exception.class)
	public void getNbOnEmptyDatabase() throws Exception {
		statement.execute("drop table if exists computer;");
		statement.execute("drop table if exists company;");
		dao.getNb();
	}
	
	@Test
	public void getById() throws Exception {
		ArrayList<Computer> listComputer = new ArrayList<Computer>();
		ArrayList<Company> listCompany = new ArrayList<Company>();
		initDBWithBasicInfo(listComputer, listCompany);
		assertTrue(dao.getById(1l).equals(listCompany.get(0)));
	}
	
	@Test(expected = Exception.class)
	public void getByIdNullParameter() throws Exception {
		ArrayList<Computer> listComputer = new ArrayList<Computer>();
		ArrayList<Company> listCompany = new ArrayList<Company>();
		initDBWithBasicInfo(listComputer, listCompany);
		dao.getById(null);
	}
	
	@Test(expected = Exception.class)
	public void getByIdOnEmptyTable() throws Exception {
		assertNull(dao.getById(1l));
	}
	
	@Test(expected = Exception.class)
	public void getByIdOnEmptyDatabase() throws Exception {
		statement.execute("drop table if exists computer;");
		statement.execute("drop table if exists company;");
		dao.getById(1l);
	}
	
	@Test(expected = Exception.class)
	public void deleteByIdOnEmptyDatabase() throws Exception {
		statement.execute("drop table if exists computer;");
		statement.execute("drop table if exists company;");
		dao.delete(1l);
	}
	
	@Test(expected = Exception.class)
	public void deleteByCompanyOnEmptyTable() throws Exception {
		dao.delete(1l);
	}
	
	@Test(expected = Exception.class)
	public void deleteWithInvalidId() throws Exception {
		dao.delete(100000l);
	}
	
	@Test(expected = Exception.class)
	public void deleteWithIdUsedByComputer() throws Exception {
		ArrayList<Computer> listComputer = new ArrayList<Computer>();
		ArrayList<Company> listCompany = new ArrayList<Company>();
		initDBWithBasicInfo(listComputer, listCompany);
		dao.delete(1l);
	}

	@Test
	public void deleteWithValidId() throws Exception {
		ArrayList<Computer> listComputer = new ArrayList<Computer>();
		ArrayList<Company> listCompany = new ArrayList<Company>();
		initDBWithBasicInfo(listComputer, listCompany);
		dao.delete(6l);
		ResultSet rs = statement.executeQuery("SELECT * FROM company WHERE id=6;");
		assertFalse(rs.next());
		rs.close();
		rs = statement.executeQuery("SELECT * FROM computer WHERE company_id=6;");
		assertFalse(rs.next());
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
