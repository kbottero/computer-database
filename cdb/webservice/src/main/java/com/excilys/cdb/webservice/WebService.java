package com.excilys.cdb.webservice;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;

import com.excilys.cdb.model.Computer;
import com.excilys.cdb.service.IService;

@Component
@Path("/Computer")
public class WebService {
	
	@Autowired
	private IService<Computer,Long> computersService;

	@GET
	@Path("/getAll")
	public Response getAll() {
		String output = computersService.getAll().toString();
		return Response.status(200).entity(output).build();
	}
	
	@GET
	@Path("/getOne")
	public Response getSome(@QueryParam("id") Long id) {
		String output = computersService.getOne(id).toString();
		return Response.status(200).entity(output).build();
	}

}