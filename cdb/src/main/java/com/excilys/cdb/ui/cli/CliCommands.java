package com.excilys.cdb.ui.cli;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.excilys.cdb.dto.CompanyDTO;
import com.excilys.cdb.dto.ComputerDTO;
import com.excilys.cdb.mapper.CompanyMapper;
import com.excilys.cdb.mapper.ComputerMapper;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.service.CompaniesService;
import com.excilys.cdb.service.ComputersService;
import com.excilys.cdb.ui.util.CompanyPage;
import com.excilys.cdb.ui.util.ComputerPage;
import com.excilys.cdb.ui.util.Page;
import com.excilys.cdb.validation.ValidatorDate;

public enum CliCommands {
	
	/**
	 * Command to display computers. Can display all of them or page by page.
	 */
	LIST_COMPUTERS ("list_computers") {
		@Override
		public void execute(Scanner s) {
			String keyWord;
			if (s.hasNext()) {
				keyWord = s.next();
				if (keyWord.equals(pages)) {
					listComputersPage(s);
				} else {
					invalidCommand(this);
				}
			} else {
				listComputers();
			}
		}
		@Override
		public String getHelp() {
			return this.getLabel()+" [pages [nbLinePerPages]]";
		}
	},
	/**
	 * Command to display companies. Can display all of them or page by page.
	 */
	LIST_COMPANIES ("list_companies") {
		@Override
		public void execute(Scanner s) {
			String keyWord;
			if (s.hasNext()) {
				keyWord = s.next();
				if (keyWord.equals(pages)) {
					listCompaniesPage(s);
				} else {
					invalidCommand(this);
				}
			} else {
				listCompanies();
			}
		}
		@Override
		public String getHelp() {
			return  this.getLabel()+" [pages [nbLinePerPages]]";
		}
	},
	/**
	 * Print data on one computer.
	 */
	SHOW ("show_details") {
		@Override
		public void execute(Scanner s) {
			if (s.hasNext()) {
				long id = s.nextLong();
				Computer computer = computersService.getOne(id);
				if (computer != null) {
					printComputer(computerMapper.toDTO(computer));
				} else {
					System.out.println("This id does not refer to any computer");
				}
			} else {
				invalidCommand(this);
			}
			
		}
		@Override
		public String getHelp() {
			return  this.getLabel()+" id";
		}
	},
	/**
	 * Update one computers.
	 */
	UPDATE ("update") {
		@Override
		public void execute(Scanner s) {
			Computer computer;
			Long id;
			Long company_id = null;
			String name = null;
			LocalDateTime introductionDate = null;
			LocalDateTime discontinuedDate = null;
			
			if (s.hasNext()) {
				id = s.nextLong();
			} else {
				invalidCommand(this);
				return;
			}
			
			if (s.hasNext()) {
				String attri = s.nextLine();
				String[] attrib = attri.trim().split(" ");
				for (String strg : attrib) {
					if ( strg.startsWith("introductionDate") ) {
						attri = strg.substring(strg.indexOf('=')+1);
						if (ValidatorDate.check(attri)) {
							introductionDate = LocalDateTime.parse(attri+"T00:00:00", DateTimeFormatter.ISO_DATE_TIME);
						} else {
							invalidCommand(this);
						}
					} else if ( strg.startsWith("discontinuedDate") ) {
						attri = strg.substring(strg.indexOf('=')+1);
						if (ValidatorDate.check(attri)) {
							discontinuedDate = LocalDateTime.parse(attri+"T00:00:00", DateTimeFormatter.ISO_DATE_TIME);
						} else {
							invalidCommand(this);
						}
					} else if ( strg.startsWith("name") ) {
						name = strg.substring(strg.indexOf('=')+1);
					} else if ( strg.startsWith("company_id") ) {
						company_id = Long.valueOf(strg.substring(strg.indexOf('=')+1));
					} else {
						invalidCommand(this);
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
				invalidCommand(this);
				return;
			}
			
			computersService.saveOne(computer);
		}
		@Override
		public String getHelp() {
			return  this.getLabel()+" id [name=new_name] [introductionDate=yyyy-MM-dd] [discontinuedDate=yyyy-MM-dd] [company_id=new_company_id]";
		}
	},
	/**
	 * Create one computers.
	 */
	CREATE ("create") {
		@Override
		public void execute(Scanner s) {
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
						if (ValidatorDate.check(attri)) {
							introductionDate = LocalDateTime.parse(attri+"T00:00:00", DateTimeFormatter.ISO_DATE_TIME);
						} else {
							invalidCommand(this);
						}
					} else if ( strg.startsWith("discontinuedDate") ) {
						attri = strg.substring(strg.indexOf('=')+1);
						if (ValidatorDate.check(attri)) {
							discontinuedDate = LocalDateTime.parse(attri+"T00:00:00", DateTimeFormatter.ISO_DATE_TIME);
						} else {
							invalidCommand(this);
						}
					} else if ( strg.startsWith("company_id") ) {
						company_id = Long.valueOf(strg.substring(strg.indexOf('=')+1));
					} else {
						invalidCommand(this);
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
		@Override
		public String getHelp() {
			return this.getLabel()+" name [introductionDate=yyyy-MM-dd] [discontinuedDate=yyyy-MM-dd] [company_id=new_company_id]";
		}
	},
	/**
	 * Delete one computers.
	 */
	DELETE_COMPUTER ("delete_computer") {
		@Override
		public void execute(Scanner s) {
			if (s.hasNext()) {
				long id = s.nextLong();
				computersService.deleteOne(id);
			} else {
				invalidCommand(this);
			}
		}
		@Override
		public String getHelp() {
			return this.getLabel()+" id";
		}
	},
	/**
	 * Delete one company.
	 */
	DELETE_COMPANY("delete_company") {
		@Override
		public void execute(Scanner s) {
			if (s.hasNext()) {
				long id = s.nextLong();
				companiesService.deleteOne(id);
			} else {
				invalidCommand(this);
			}
		}
		@Override
		public String getHelp() {
			return this.getLabel()+" id";
		}
	},
	/**
	 * List all the existing commands.
	 */
	HELP ("help") {
		@Override
		public void execute(Scanner s) {
			System.out.println("Help : list commands");
			for (CliCommands commands : CliCommands.values()) {
				System.out.println(commands.getHelp());
			}
		}
		@Override
		public String getHelp() {
			return "help : return list of commands";
		}
	},
	/**
	 * Command to quit the CLI
	 */
	QUIT ("quit") {
		@Override
		public void execute(Scanner s) {
		
		}
		@Override
		public String getHelp() {
			return this.getLabel();
		}
	};
	
	private final String label;

	private static ComputersService computersService;
	private static CompaniesService companiesService;
	private static ComputerMapper computerMapper;
	private static CompanyMapper companyMapper;
	private static String invalidCommand = "Invalid Command";
	private static String pages = "pages";
	public static String prompt = ">";
	public static String quitPages = "q";
	
	private static Map<String,CliCommands> listCommands;
	static {
		listCommands = new HashMap<String,CliCommands>();
		for (CliCommands cliCom : CliCommands.values()) {
				listCommands.put(cliCom.getLabel(), cliCom);
		}
		ApplicationContext context = new ClassPathXmlApplicationContext("spring-config.xml");
		computersService = (ComputersService) context.getBean("computersService");
		companiesService = (CompaniesService) context.getBean("companiesService");
		computerMapper = (ComputerMapper) context.getBean("computerMapper");
		companyMapper = (CompanyMapper) context.getBean("companyMapper");
	}
	
	CliCommands(String label) {
        this.label = label;
    }
	
	public String getLabel() {
		return label;
	}
	
	public String getHelp() {
		return this.name();
	}
	
	public abstract void execute(Scanner s);
	
	public static CliCommands getCommands(Scanner s) {
		if (s.hasNext()) {
			String comm = s.next();
			if (! comm.isEmpty()) {
				CliCommands com = listCommands.get(comm);
				if (com != null) {
					return com;
				}
			}
		}
		return CliCommands.HELP;
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
		if (c.getConstructorName()!= null) {
			strBuild.append(c.getConstructorName());
			strBuild.append("\t");
		}
		strBuild.append(c.getIntroductionDate());
		strBuild.append("\t");
		strBuild.append(c.getDiscontinuedDate());
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
	private static void listComputers() {
		ArrayList<Computer> list = new ArrayList<Computer>();
		list = (ArrayList<Computer>) computersService.getAll();
		for (Computer computer : list) {
			printComputer(computerMapper.toDTO(computer));
		}
	}

	/**
	 * Display the list of companies within the Database
	 */
	private static void listCompanies() {
		ArrayList<Company> list = new ArrayList<Company>();
		list = (ArrayList<Company>) companiesService.getAll();
		for (Company company : list) {
			printCompany(companyMapper.toDTO(company));
		}
	}

	/**
	 * Display page by page the list of computers within the Database,
	 * 
	 */
	private static void listComputersPage(Scanner s) {
		Page<Computer, Long> page;
		if (s.hasNext()) {
			Long nbLine = s.nextLong();
			page = new ComputerPage (computersService, nbLine,null);
		} else {
			page = new ComputerPage (computersService,20l,null);
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
	private static void listCompaniesPage(Scanner s) {
		Page<Company, Long> page;
		if (s.hasNext()) {
			Long nbLine = s.nextLong();
			page = new CompanyPage (companiesService, nbLine,null);
		} else {
			page = new CompanyPage (companiesService,null,null);
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
	private static void invalidCommand(CliCommands comm) {
		System.out.print(invalidCommand);
		System.out.print(" : ");
		System.out.println(comm.getHelp());
	}
	
	/**
	 * Print information related to the current page
	 */
	private static void printEndOfPage (Page<?,?> page) {
		StringBuilder stgBuild = new StringBuilder();
		stgBuild.append("Page ");
		stgBuild.append(page.getCurrent());
		stgBuild.append("/");
		stgBuild.append(page.getCount());
		stgBuild.append("\t (enter '");
		stgBuild.append(CliCommands.quitPages);
		stgBuild.append("' to quit)");
		System.out.println(stgBuild.toString());
	}
}
