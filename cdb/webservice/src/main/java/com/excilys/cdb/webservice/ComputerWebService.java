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
	@Path("/get/all")
	public List<ComputerDTO> getAll() {
		return computerMapper.toDTOList(computersService.getAll());
	}
	
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/get/all/page")
	public List<ComputerDTO> getAll(Page<Computer, Long>  page) {
		return computerMapper.toDTOList(computersService.getAll(page));
	}
	
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/get/page")
	public List<ComputerDTO> getSome(Page<Computer, Long>  page) {
		return computerMapper.toDTOList(computersService.getSome(page));
	}
	
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/save")
	public Response saveOne(Computer computer) {
		computersService.saveOne(computer);
		return Response.status(200).entity("Save").build();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/get/{id}")
	public ComputerDTO getSome(@QueryParam("id") Long id) {
		return computerMapper.toDTO(computersService.getOne(id));
	}
	
	@GET
	@Path("/nb")
	public String getNbComputer() {
		return computersService.getNbInstance().toString();
	}
	
	@GET
	@Path("/delete")
	public Response delete(@QueryParam("id") Long id) {
		computersService.deleteOne(id);
		return Response.status(200).entity("Delete").build();
	}
}