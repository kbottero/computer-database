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

import com.excilys.cdb.dto.ComputerDTO;
import com.excilys.cdb.mapper.model.IMapper;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.page.Page;
import com.excilys.cdb.service.IService;

@Component
@Path("/Computer")
public class ComputerWebService {
	
	@Autowired
	private IService<Computer,Long> computersService;
	@Autowired
	private IMapper<Computer,ComputerDTO> computerMapper;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getAll")
	public List<ComputerDTO> getAll() {
		return computerMapper.toDTOList(computersService.getAll());
	}
	
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getAllPaged")
	public Response getAll(Page<Computer, Long>  page) {
		String output = computersService.getAll(page).toString();
		return Response.status(200).entity(output).build();
	}
	
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getSome")
	public Response getSome(Page<Computer, Long>  page) {
		String output = computersService.getSome(page).toString();
		return Response.status(200).entity(output).build();
	}
	
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/save")
	public Response saveOne(Computer computer) {
		computersService.saveOne(computer);
		return Response.status(200).build();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getOne")
	public Response getSome(@QueryParam("id") Long id) {
		String output = computersService.getOne(id).toString();
		return Response.status(200).entity(output).build();
	}
	
	@GET
	@Path("/NbComputer")
	public Response getNbComputer() {
		String output = computersService.getNbInstance().toString();
		return Response.status(200).entity(output).build();
	}
	
	@GET
	@Path("/delete")
	public Response delete(@QueryParam("id") Long id) {
		computersService.deleteOne(id);
		return Response.status(200).build();
	}
}