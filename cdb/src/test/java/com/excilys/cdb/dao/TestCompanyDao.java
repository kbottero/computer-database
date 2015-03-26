package com.excilys.cdb.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.excilys.cdb.exception.DaoException;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.persistence.CDBTransaction;
import com.excilys.cdb.persistence.CompanyDao;
import com.excilys.cdb.persistence.DaoManager;
import com.excilys.cdb.persistence.DaoRequestParameter;
import com.excilys.cdb.persistence.CDBTransaction.TransactionType;
import com.excilys.cdb.persistence.DaoRequestParameter.Order;

/**
 * TODO: Complete with DbUnit
 * @author Kevin Bottero
 *
 */
public class TestCompanyDao {

	private Connection connection;
	private Statement statement;
	
	@BeforeClass
	public static void setUpDB () {
		System.setProperty("env", "TEST");
	}

	@Before
	public void setUp() throws Exception {
		
		BufferedReader in = new BufferedReader(new InputStreamReader(DaoManager.class.getClassLoader().getResourceAsStream("db-test.properties")));
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
		CDBTransaction transaction = new CDBTransaction(TransactionType.ATOMIC);
		transaction.init();
		ArrayList<Computer> listComputer = new ArrayList<Computer>();
		ArrayList<Company> listCompany = new ArrayList<Company>();
		initDBWithBasicInfo(listComputer, listCompany);
		
		ArrayList<Company> result = (ArrayList<Company>) CompanyDao.INSTANCE.getAll(transaction);
		
		assertTrue(	listCompany.size() == result.size());
		for (int i=0; i<listCompany.size() ;++i) {
			assertTrue(result.get(i).equals(listCompany.get(i)));
		}
		transaction.close();
	}
	
	@Test(expected = DaoException.class)
	public void getAllOnEmptyDatabase() throws Exception {
		CDBTransaction transaction = new CDBTransaction(TransactionType.ATOMIC);
		transaction.init();
		statement.execute("drop table if exists computer;");
		statement.execute("drop table if exists company;");
		CompanyDao.INSTANCE.getAll(transaction);
		transaction.close();
	}
	
	@Test(expected = NullPointerException.class)
	public void getAllWithNullParam() throws Exception {
		CDBTransaction transaction = new CDBTransaction(TransactionType.ATOMIC);
		transaction.init();
		ArrayList<Computer> listComputer = new ArrayList<Computer>();
		ArrayList<Company> listCompany = new ArrayList<Company>();
		initDBWithBasicInfo(listComputer, listCompany);
		CompanyDao.INSTANCE.getAll(transaction,null);
		transaction.close();
	}
	
	@Test
	public void getAllWithOrderByName() throws Exception {
		CDBTransaction transaction = new CDBTransaction(TransactionType.ATOMIC);
		transaction.init();
		ArrayList<Computer> listComputer = new ArrayList<Computer>();
		ArrayList<Company> listCompany = new ArrayList<Company>();
		initDBWithBasicInfo(listComputer, listCompany);
		ArrayList<String> list = new ArrayList<String>();
		list.add("name");
		DaoRequestParameter param = new DaoRequestParameter (null,null,list,Order.DESC,null,null);
		ArrayList<Company> result = (ArrayList<Company>) CompanyDao.INSTANCE.getAll(transaction,param);
		
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
		transaction.close();
	}
	
	@Test
	public void getAllWithOrderByNameAndId() throws Exception {
		CDBTransaction transaction = new CDBTransaction(TransactionType.ATOMIC);
		transaction.init();
		ArrayList<Computer> listComputer = new ArrayList<Computer>();
		ArrayList<Company> listCompany = new ArrayList<Company>();
		initDBWithBasicInfo(listComputer, listCompany);
		ArrayList<String> list = new ArrayList<String>();
		list.add("name");
		list.add("id");
		DaoRequestParameter param = new DaoRequestParameter (null,null,list,Order.DESC,null,null);
		ArrayList<Company> result = (ArrayList<Company>) CompanyDao.INSTANCE.getAll(transaction,param);
		
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
		transaction.close();
	}
	
	@Test(expected = NullPointerException.class)
	public void getSomeWithNullParameter() throws Exception {
		CDBTransaction transaction = new CDBTransaction(TransactionType.ATOMIC);
		transaction.init();
		CompanyDao.INSTANCE.getSome(transaction,null);
		transaction.close();
	}

	@Test(expected = DaoException.class)
	public void getSomeWithRequestParameterAttributeNull() throws Exception {
		CDBTransaction transaction = new CDBTransaction(TransactionType.ATOMIC);
		transaction.init();
		ArrayList<Company> list = (ArrayList<Company>) CompanyDao.INSTANCE.getSome(transaction,new DaoRequestParameter(null, null, null, null, null, null));
		assertNotNull(list);
		assertNotNull(list.size());
		transaction.close();
	}
	
	@Test
	public void getSomeWithFilter() throws Exception {
		CDBTransaction transaction = new CDBTransaction(TransactionType.ATOMIC);
		transaction.init();
		ArrayList<Computer> listComputer = new ArrayList<Computer>();
		ArrayList<Company> listCompany = new ArrayList<Company>();
		initDBWithBasicInfo(listComputer, listCompany);
		ArrayList<Company> list = (ArrayList<Company>) CompanyDao.INSTANCE.getSome(transaction,new DaoRequestParameter(DaoRequestParameter.NameFiltering.POST, "T", null, null, 10l, null));
		assertEquals(list.size(),2);
		transaction.close();
	}
	
	@Test(expected = DaoException.class)
	public void getSomeOnEmptyDatabase() throws Exception {
		CDBTransaction transaction = new CDBTransaction(TransactionType.ATOMIC);
		transaction.init();
		statement.execute("drop table if exists computer;");
		statement.execute("drop table if exists company;");
		CompanyDao.INSTANCE.getSome(transaction,new DaoRequestParameter(null, null, null, null, 10l, null));
		transaction.close();
	}
	
	@Test
	public void getSomeWithOrderByName() throws Exception {
		CDBTransaction transaction = new CDBTransaction(TransactionType.ATOMIC);
		transaction.init();
		ArrayList<Computer> listComputer = new ArrayList<Computer>();
		ArrayList<Company> listCompany = new ArrayList<Company>();
		initDBWithBasicInfo(listComputer, listCompany);
		ArrayList<String> list = new ArrayList<String>();
		list.add("name");
		DaoRequestParameter param = new DaoRequestParameter (null,null,list,Order.DESC,4l,null);
		ArrayList<Company> result = (ArrayList<Company>) CompanyDao.INSTANCE.getSome(transaction,param);
		
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
		transaction.close();
	}
	
	@Test
	public void getSomeWithOrderByNameAndOffset() throws Exception {
		CDBTransaction transaction = new CDBTransaction(TransactionType.ATOMIC);
		transaction.init();
		ArrayList<Computer> listComputer = new ArrayList<Computer>();
		ArrayList<Company> listCompany = new ArrayList<Company>();
		initDBWithBasicInfo(listComputer, listCompany);
		ArrayList<String> list = new ArrayList<String>();
		list.add("name");
		DaoRequestParameter param = new DaoRequestParameter (null,null,list,Order.DESC,4l,1l);
		ArrayList<Company> result = (ArrayList<Company>) CompanyDao.INSTANCE.getSome(transaction,param);
		
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
		transaction.close();
	}
	
	@Test
	public void getNb() throws Exception {
		CDBTransaction transaction = new CDBTransaction(TransactionType.ATOMIC);
		transaction.init();
		ArrayList<Computer> listComputer = new ArrayList<Computer>();
		ArrayList<Company> listCompany = new ArrayList<Company>();
		initDBWithBasicInfo(listComputer, listCompany);
		assertTrue(CompanyDao.INSTANCE.getNb(transaction) == listCompany.size());
		transaction.close();
	}
	
	@Test
	public void getNbOnEmptyTable() throws Exception {
		CDBTransaction transaction = new CDBTransaction(TransactionType.ATOMIC);
		transaction.init();
		assertEquals(new Long (0l),CompanyDao.INSTANCE.getNb(transaction));
		transaction.close();
	}

	@Test(expected = DaoException.class)
	public void getNbOnEmptyDatabase() throws Exception {
		CDBTransaction transaction = new CDBTransaction(TransactionType.ATOMIC);
		transaction.init();
		statement.execute("drop table if exists computer;");
		statement.execute("drop table if exists company;");
		CompanyDao.INSTANCE.getNb(transaction);
		transaction.close();
	}
	
	@Test
	public void getById() throws Exception {
		CDBTransaction transaction = new CDBTransaction(TransactionType.ATOMIC);
		transaction.init();
		ArrayList<Computer> listComputer = new ArrayList<Computer>();
		ArrayList<Company> listCompany = new ArrayList<Company>();
		initDBWithBasicInfo(listComputer, listCompany);
		assertTrue(CompanyDao.INSTANCE.getById(transaction,1l).equals(listCompany.get(0)));
		transaction.close();
	}
	
	@Test(expected = DaoException.class)
	public void getByIdNullParameter() throws Exception {
		CDBTransaction transaction = new CDBTransaction(TransactionType.ATOMIC);
		transaction.init();
		ArrayList<Computer> listComputer = new ArrayList<Computer>();
		ArrayList<Company> listCompany = new ArrayList<Company>();
		initDBWithBasicInfo(listComputer, listCompany);
		CompanyDao.INSTANCE.getById(transaction,null);
		transaction.close();
	}
	
	@Test(expected = DaoException.class)
	public void getByIdOnEmptyTable() throws Exception {
		CDBTransaction transaction = new CDBTransaction(TransactionType.ATOMIC);
		transaction.init();
		assertNull(CompanyDao.INSTANCE.getById(transaction,1l));
		transaction.close();
	}
	
	@Test(expected = DaoException.class)
	public void getByIdOnEmptyDatabase() throws Exception {
		CDBTransaction transaction = new CDBTransaction(TransactionType.ATOMIC);
		transaction.init();
		statement.execute("drop table if exists computer;");
		statement.execute("drop table if exists company;");
		CompanyDao.INSTANCE.getById(transaction,1l);
		transaction.close();
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
