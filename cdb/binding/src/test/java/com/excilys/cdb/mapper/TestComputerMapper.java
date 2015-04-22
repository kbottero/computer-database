package com.excilys.cdb.mapper;

import static org.junit.Assert.assertEquals;

import java.sql.Connection;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.excilys.cdb.dto.ComputerDTO;
import com.excilys.cdb.mapper.model.impl.ComputerMapper;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/spring-context-test.xml"})
public class TestComputerMapper {

	private Connection connection = null;
	private Statement statement = null;
	@Autowired
	private ComputerMapper computerMapper;
	
	@BeforeClass
	public static void setUpDB () {
		System.setProperty("env", "TEST");
	}
	
	@Before
	public void setUp() throws Exception {
		
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void toDTOWithMinimunData() {
		Computer computer = new Computer(1l,"Company");
		ComputerDTO computerDTO = new ComputerDTO(1l,"Company","","",0l,"");
		assertEquals(computerDTO, computerMapper.toDTO(computer));
	}

	@Test
	public void fromDTOWithMinimunData() {
		Computer computer = new Computer(1l,"Company");
		ComputerDTO computerDTO = new ComputerDTO(1l,"Company","","",0l,"");
		assertEquals(computer, computerMapper.fromDTO(computerDTO));
	}

	@Test
	public void toDTOWithAllData() {
		LocalDateTime localDateTime = LocalDateTime.now();
		Computer computer = new Computer(1l,"Company",localDateTime,localDateTime.plusDays(50),new Company (1l, "company"));
		ComputerDTO computerDTO = new ComputerDTO(1l,"Company",localDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE),localDateTime.plusDays(50).format(DateTimeFormatter.ISO_LOCAL_DATE),1l,"company");
		assertEquals(computerDTO, computerMapper.toDTO(computer));
	}

	@Test
	public void fromDTOWithAllData() {
		LocalDateTime localDateTime = LocalDateTime.now();
		Computer computer = new Computer(1l,"Company",localDateTime,localDateTime.plusDays(50),new Company (1l, "company"));
		ComputerDTO computerDTO = new ComputerDTO(1l,"Company",localDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE),localDateTime.plusDays(50).format(DateTimeFormatter.ISO_LOCAL_DATE),1l,"company");
		assertEquals(computer, computerMapper.fromDTO(computerDTO));
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
