package com.excilys.cdb.mapper;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.excilys.cdb.dto.CompanyDTO;
import com.excilys.cdb.mapper.model.impl.CompanyMapper;
import com.excilys.cdb.model.Company;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:/spring-context-test.xml"})
public class TestCompanyMapper {

	private CompanyMapper companyMapper;
	
	@Before
	public void setUp() throws Exception {
		companyMapper = new CompanyMapper();
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void toDTO() {
		Company company = new Company(1l,"Company");
		CompanyDTO companyDTO = new CompanyDTO(1l,"Company");
		assertEquals(companyDTO, companyMapper.toDTO(company));
	}

	@Test
	public void fromDTO() {
		Company company = new Company(1l,"Company");
		CompanyDTO companyDTO = new CompanyDTO(1l,"Company");
		assertEquals(company, companyMapper.fromDTO(companyDTO));
	}
}
