package com.excilys.cdb.webservice;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.excilys.cdb.dto.CompanyDTO;
import com.excilys.cdb.mapper.model.IMapper;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.page.Page;
import com.excilys.cdb.service.IService;

@Component
@Path("/Company")
public class CompanyWebService {
	
	@Autowired
	private IService<Company,Long> companiesService;
	@Autowired
	private IMapper<Company,CompanyDTO> companyMapper;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/get/all")
	public List<CompanyDTO> getAll() {
		return companyMapper.toDTOList(companiesService.getAll());
	}
	
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/get/all/page")
	public List<CompanyDTO> getAll(Page<Company, Long>  page) {
		return companyMapper.toDTOList(companiesService.getAll(page));
	}
	
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/get/page")
	public List<CompanyDTO> getSome(Page<Company, Long>  page) {
		return companyMapper.toDTOList(companiesService.getSome(page));
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/get/{id}")
	public CompanyDTO getSome(@QueryParam("id") Long id) {
		return companyMapper.toDTO(companiesService.getOne(id));
	}
	
	@GET
	@Path("/delete")
	public Response delete(@QueryParam("id") Long id) {
		companiesService.deleteOne(id);
		return Response.status(200).entity("delete").build();
	}
}
