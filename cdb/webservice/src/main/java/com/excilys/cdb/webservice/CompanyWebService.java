package com.excilys.cdb.webservice;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.page.Page;
import com.excilys.cdb.service.IService;

@Component
@Path("/Company")
public class CompanyWebService {
	
	@Autowired
	private IService<Company,Long> companiesService;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getAll")
	public Response getAll() {
		String output = companiesService.getAll().toString();
		return Response.status(200).entity(output).build();
	}
	
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getAll")
	public Response getAll(Page<Company, Long>  page) {
		String output = companiesService.getAll(page).toString();
		return Response.status(200).entity(output).build();
	}
	
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getSome")
	public Response getSome(Page<Company, Long>  page) {
		String output = companiesService.getSome(page).toString();
		return Response.status(200).entity(output).build();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getOne")
	public Response getSome(@QueryParam("id") Long id) {
		String output = companiesService.getOne(id).toString();
		return Response.status(200).entity(output).build();
	}
	
	@GET
	@Path("/delete")
	public Response delete(@QueryParam("id") Long id) {
		companiesService.deleteOne(id);
		return Response.status(200).build();
	}
}
