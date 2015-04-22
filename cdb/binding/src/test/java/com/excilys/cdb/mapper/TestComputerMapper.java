package com.excilys.cdb.mapper;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.After;
import org.junit.Before;
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
@ContextConfiguration(locations={"classpath:/application-context-test.xml"})
public class TestComputerMapper {

	@Autowired
	private ComputerMapper computerMapper;
	
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void toDTOWithMinimunData() {
		Computer computer = new Computer(1l,"Computer");
		ComputerDTO computerDTO = new ComputerDTO(1l,"Computer","","",0l,"");
		assertEquals(computerDTO, computerMapper.toDTO(computer));
	}

	@Test
	public void fromDTOWithMinimunData() {
		Computer computer = new Computer(1l,"Computer");
		ComputerDTO computerDTO = new ComputerDTO(1l,"Computer","","",0l,"");
		assertEquals(computer, computerMapper.fromDTO(computerDTO));
	}

	@Test
	public void toDTOWithAllData() {
		LocalDateTime localDateTime = LocalDateTime.now();
		Computer computer = new Computer(1l,"Computer",localDateTime,localDateTime.plusDays(50),new Company (1l, "company"));
		ComputerDTO computerDTO = new ComputerDTO(1l,"Computer",localDateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),localDateTime.plusDays(50).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),1l,"company");
		assertEquals(computerDTO, computerMapper.toDTO(computer));
	}

	@Test
	public void fromDTOWithAllData() {
		LocalDateTime localDateTime = LocalDateTime.now();
		Computer computer = new Computer(1l,"Computer",localDateTime,localDateTime.plusDays(50),new Company (1l, "company"));
		ComputerDTO computerDTO = new ComputerDTO(1l,"Computer",localDateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),localDateTime.plusDays(50).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),1l,"company");
		assertEquals(computer, computerMapper.fromDTO(computerDTO));
	}
}
