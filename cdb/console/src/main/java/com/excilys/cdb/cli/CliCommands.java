package com.excilys.cdb.cli;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.excilys.cdb.dto.CompanyDTO;
import com.excilys.cdb.dto.ComputerDTO;
import com.excilys.cdb.mapper.model.IMapper;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.page.Page;
import com.excilys.cdb.page.impl.CompanyPage;
import com.excilys.cdb.page.impl.ComputerPage;
import com.excilys.cdb.service.IService;
import com.excilys.cdb.validation.ValidatorLocalDateTime;

@Component
public class CliCommands {
	
	@Autowired
	private IService<Computer,Long> computersService;
	@Autowired
	private IService<Company,Long> companiesService;
	@Autowired
	private IMapper<Computer,ComputerDTO> computerMapper;
	@Autowired
	private IMapper<Company,CompanyDTO>  companyMapper;
	
	private static final String SERVER_URL = "http://localhost:8080/webservice/rest/";
	
	private static String invalidCommand = "Invalid Command";
	private static String pages = "pages";
	public static String prompt = ">";
	public static String quitPages = "q";
	
	public enum Commands {
		LIST_COMPUTERS ("list_computers"),
		LIST_COMPANIES ("list_companies"),
		SHOW ("show_details"),
		UPDATE ("update"),
		CREATE ("create"),
		DELETE_COMPUTER ("delete_computer"),
		DELETE_COMPANY ("delete_company"),
		HELP ("help"),
		QUIT ("quit");
		
		private final String label;

		private static Map<String,Commands> listCommands;
		static {
			listCommands = new HashMap<String,Commands>();
			for (Commands cliCom : Commands.values()) {
					listCommands.put(cliCom.getLabel(), cliCom);
			}
		}
		
		Commands(String label) {
	        this.label = label;
	    }
		
		public String getLabel() {
			return label;
		}
	}
	
	/**
	 * Command to display computers. Can display all of them or page by page.
	 */
	public void listComputer(Scanner s) {
		String keyWord;
		if (s.hasNext()) {
			keyWord = s.next();
			if (keyWord.equals(pages)) {
				listComputersPage(s);
			} else {
				invalidCommand(Commands.LIST_COMPUTERS);
			}
		} else {
			listComputers();
		}
	}
	
	public static String getHelp(Commands c) {
		switch (c) {
		case LIST_COMPUTERS :
			return c.getLabel()+" [pages [nbLinePerPages]]";
		
		case LIST_COMPANIES :
			return  c.getLabel()+" [pages [nbLinePerPages]]";
		
		case SHOW :
			return  c.getLabel()+" id";
			
		case UPDATE :	
			return  c.getLabel()+" id [name=new_name] [introductionDate=yyyy-MM-dd] [discontinuedDate=yyyy-MM-dd] [company_id=new_company_id]";
		
		case CREATE:	
			return c.getLabel()+" name [introductionDate=yyyy-MM-dd] [discontinuedDate=yyyy-MM-dd] [company_id=new_company_id]";
			
		case DELETE_COMPUTER:
			return c.getLabel()+" id";
			
		case DELETE_COMPANY:
			return c.getLabel()+" id";
			
		case HELP :
			return "help : return list of commands";
			
		case QUIT:
			return "quit";
		
		default:
			StringBuilder strgBuild = new StringBuilder();
			strgBuild.append("Help : list commands");
			for (Commands commands : Commands.values()) {
				strgBuild.append(getHelp(commands));
			}
			return strgBuild.toString();
		}
	}

	public Commands getCommands(Scanner s) {
		if (s.hasNext()) {
			String comm = s.next();
			if (! comm.isEmpty()) {
				Commands com = Commands.listCommands.get(comm);
				if (com != null) {
					switch (com) {
					case LIST_COMPUTERS :
						listComputer(s);
					break;
					case LIST_COMPANIES :
						listCompany(s);
					break;
					case SHOW :
						show(s);
					break;
					case UPDATE :
						update(s);
					break;
					case CREATE:
						create(s);
					break;
					case DELETE_COMPUTER:
						deleteComputer(s);
					break;
					case DELETE_COMPANY:
						deleteCompany(s);
					break;
					case HELP :
						help();
					break;
					case QUIT:
					break;
					default:
						help();
					break;
					}
					return com;
				}
			}
		}
		return Commands.HELP;
	}

	/**
	 * Command to display companies. Can display all of them or page by page.
	 */
	public void listCompany (Scanner s) {
		String keyWord;
		if (s.hasNext()) {
			keyWord = s.next();
			if (keyWord.equals(pages)) {
				listCompaniesPage(s);
			} else {
				invalidCommand(Commands.LIST_COMPANIES);
			}
		} else {
			listCompanies();
		}
	}
	
	/**
	 * Print data on one computer.
	 */
	public void show(Scanner s) {
		if (s.hasNext()) {
			long id = s.nextLong();
			Computer computer = computersService.getOne(id);
			if (computer != null) {
				printComputer(computerMapper.toDTO(computer));
			} else {
				System.out.println("This id does not refer to any computer");
			}
		} else {
			invalidCommand(Commands.SHOW);
		}
	}
	
	/**
	 * Update one computers.
	 */
	public void update(Scanner s) {
		Computer computer;
		Long id;
		Long company_id = null;
		String name = null;
		LocalDateTime introductionDate = null;
		LocalDateTime discontinuedDate = null;
		
		if (s.hasNext()) {
			id = s.nextLong();
		} else {
			invalidCommand(Commands.UPDATE);
			return;
		}
		
		if (s.hasNext()) {
			String attri = s.nextLine();
			String[] attrib = attri.trim().split(" ");
			for (String strg : attrib) {
				if ( strg.startsWith("introductionDate") ) {
					attri = strg.substring(strg.indexOf('=')+1);
					if (ValidatorLocalDateTime.check(attri)) {
						introductionDate = LocalDateTime.parse(attri+"T00:00:00", DateTimeFormatter.ISO_DATE_TIME);
					} else {
						invalidCommand(Commands.UPDATE);
					}
				} else if ( strg.startsWith("discontinuedDate") ) {
					attri = strg.substring(strg.indexOf('=')+1);
					if (ValidatorLocalDateTime.check(attri)) {
						discontinuedDate = LocalDateTime.parse(attri+"T00:00:00", DateTimeFormatter.ISO_DATE_TIME);
					} else {
						invalidCommand(Commands.UPDATE);
					}
				} else if ( strg.startsWith("name") ) {
					name = strg.substring(strg.indexOf('=')+1);
				} else if ( strg.startsWith("company_id") ) {
					company_id = Long.valueOf(strg.substring(strg.indexOf('=')+1));
				} else {
					invalidCommand(Commands.UPDATE);
					return;
				}
			}
		}

		computer = computersService.getOne(id);
		
		if (computer != null) {
			if (name != null) {
				computer.setName(name);
			}
			if (introductionDate != null) {
				computer.setIntroductionDate(introductionDate);
			}
			if (discontinuedDate != null) {
				computer.setDiscontinuedDate(discontinuedDate);
			}
			if (company_id != null) {
				computer.setConstructor(companiesService.getOne(company_id));
			}
		} else {
			invalidCommand(Commands.UPDATE);
			return;
		}
		
		computersService.saveOne(computer);
	}
	
	/**
	 * Create one computers.
	 */
	public void create(Scanner s) {
		Computer computer;
		String name;
		Long company_id = null;
		LocalDateTime introductionDate = null;
		LocalDateTime discontinuedDate = null;

		if (s.hasNext()) {
			name = s.next();
		} else {
			System.out.println(invalidCommand);
			return;
		}
		
		computer = new Computer(-1,name);
		
		if (s.hasNext()) {
			String attri = s.nextLine();
			String[] attrib = attri.trim().split(" ");
			for (String strg : attrib) {
				if ( strg.startsWith("introductionDate") ) {
					attri = strg.substring(strg.indexOf('=')+1);
					if (ValidatorLocalDateTime.check(attri)) {
						introductionDate = LocalDateTime.parse(attri+"T00:00:00", DateTimeFormatter.ISO_DATE_TIME);
					} else {
						invalidCommand(Commands.CREATE);
					}
				} else if ( strg.startsWith("discontinuedDate") ) {
					attri = strg.substring(strg.indexOf('=')+1);
					if (ValidatorLocalDateTime.check(attri)) {
						discontinuedDate = LocalDateTime.parse(attri+"T00:00:00", DateTimeFormatter.ISO_DATE_TIME);
					} else {
						invalidCommand(Commands.CREATE);
					}
				} else if ( strg.startsWith("company_id") ) {
					company_id = Long.valueOf(strg.substring(strg.indexOf('=')+1));
				} else {
					invalidCommand(Commands.CREATE);
					return;
				}
			}
		}
		
		if (introductionDate != null) {
			computer.setIntroductionDate(introductionDate);
		}
		if (discontinuedDate != null) {
			computer.setDiscontinuedDate(discontinuedDate);
		}
		if (company_id != null) {
			computer.setConstructor(companiesService.getOne(company_id));
		}
		computersService.saveOne(computer);
		if (computer.getId() != -1) {
			System.out.println("Computer created. Id = "+computer.getId());
		}
	}
	/**
	 * Delete one computers.
	 */
	public void deleteComputer(Scanner s) {
		if (s.hasNext()) {
			long id = s.nextLong();
			computersService.deleteOne(id);
		} else {
			invalidCommand(Commands.DELETE_COMPUTER);
		}
	}
	/**
	 * Delete one company.
	 */
	public void deleteCompany(Scanner s) {
		if (s.hasNext()) {
			long id = s.nextLong();
			companiesService.deleteOne(id);
		} else {
			invalidCommand(Commands.DELETE_COMPANY);
		}
	}
	/**
	 * List all the existing commands.
	 */
	public static void help() {
		System.out.println("Help : list commands");
		for (Commands commands : Commands.values()) {
			System.out.println(getHelp(commands));
		}
	}

	/**
	 * Display a computer
	 */
	public static void printComputer(ComputerDTO c) {
		StringBuilder strBuild = new StringBuilder();
		strBuild.append(c.getId());
		strBuild.append("\t");
		strBuild.append(c.getName());
		strBuild.append("\t");
		if (c.getCompanyName()!= null) {
			strBuild.append(c.getCompanyName());
			strBuild.append("\t");
		}
		strBuild.append(c.getIntroduced());
		strBuild.append("\t");
		strBuild.append(c.getDiscontinued());
		System.out.println(strBuild.toString());
	}
	
	/**
	 * Display a company
	 */
	public static void printCompany(CompanyDTO c) {
		StringBuilder strBuild = new StringBuilder();
		strBuild.append(c.getId());
		strBuild.append("\t");
		strBuild.append(c.getName());
		System.out.println(strBuild.toString());
	}

	/**
	 * Display the list of computers within the Database
	 */
	private void listComputers() {
		RestTemplate restTemplate = new RestTemplate();
		ComputerDTO[] arrayComputerDTOs = restTemplate.getForObject(SERVER_URL+"Computer/get/all", ComputerDTO[].class);
		for (ComputerDTO computer : arrayComputerDTOs) {
			printComputer(computer);
		}
	}

	/**
	 * Display the list of companies within the Database
	 */
	private void listCompanies() {
		RestTemplate restTemplate = new RestTemplate();
		CompanyDTO[] arrayCompanyDTOs = restTemplate.getForObject(SERVER_URL+"Company/get/all", CompanyDTO[].class);
		for (CompanyDTO company : arrayCompanyDTOs) {
			printCompany(company);
		}
	}

	/**
	 * Display page by page the list of computers within the Database
	 */
	private void listComputersPage(Scanner s) {
		Page<Computer, Long> page;
		if (s.hasNext()) {
			Integer nbLine = s.nextInt();
			page = new ComputerPage (computersService,nbLine);
		} else {
			page = new ComputerPage (computersService,20);
		}
	    BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
		Scanner scan = null;
		do {
			for (ComputerDTO computer  : computerMapper.toDTOList(page.getElements())) {
				CliCommands.printComputer(computer);
			}
			printEndOfPage(page);
			System.out.print(prompt);
			try{
				scan = new Scanner(bufferRead.readLine());
				if (scan.hasNext()) {
					String keyWord = scan.next();
					if(keyWord.equals(quitPages)) {
						break;
					}
				}
			} catch(IOException e) {
				e.printStackTrace();
				break;
			}
		}while(page.nextPage());
		if (scan != null) {
			scan.close();
		}
	}
	
	/**
	 * Display page by page the list of companies within the Database,
	 * @param s Current Scanner value
	 */
	private void listCompaniesPage(Scanner s) {
		Page<Company, Long> page;
		if (s.hasNext()) {
			Integer nbLine = s.nextInt();
			page = new CompanyPage (companiesService,nbLine);
		} else {
			page = new CompanyPage (companiesService,20);
		}
	    BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
		Scanner scan = null;
		do {
			for (CompanyDTO company  : companyMapper.toDTOList(page.getElements())) {
				CliCommands.printCompany(company);
			}
			printEndOfPage(page);
			System.out.print(prompt);
			try{
				scan = new Scanner(bufferRead.readLine());
				if (scan.hasNext()) {
					String keyWord = scan.next();
					if(keyWord.equals(quitPages)) {
						break;
					}
				}
			} catch(IOException e) {
				e.printStackTrace();
				break;
			}
		} while(page.nextPage());
		if (scan != null) {
			scan.close();
		}
	}
	
	/**
	 * Display a message relative to an invalid command.
	 * @param comm
	 */
	private void invalidCommand(Commands comm) {
		System.out.print(invalidCommand);
		System.out.print(" : ");
		System.out.println(getHelp(comm));
	}
	
	/**
	 * Print information related to the current page
	 */
	private void printEndOfPage (Page<?,?> page) {
		StringBuilder stgBuild = new StringBuilder();
		stgBuild.append("Page ");
		stgBuild.append(page.getPageNumber());
		stgBuild.append("/");
		stgBuild.append(page.getNumberOfPages());
		stgBuild.append("\t (enter '");
		stgBuild.append(CliCommands.quitPages);
		stgBuild.append("' to quit)");
		System.out.println(stgBuild.toString());
	}
}
