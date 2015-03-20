package com.excilys.cdb.cli;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.service.IService;
import com.excilys.cdb.service.ServiceImpl;
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
				Computer computer = serv.getOneComputer(id);
				if (computer != null) {
					printComputer(computer);
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
						if (ValidatorDate.INSTANCE.validateDate(attri)) {
							introductionDate = LocalDateTime.parse(attri+"T00:00:00", DateTimeFormatter.ISO_DATE_TIME);
						} else {
							invalidCommand(this);
						}
					} else if ( strg.startsWith("discontinuedDate") ) {
						attri = strg.substring(strg.indexOf('=')+1);
						if (ValidatorDate.INSTANCE.validateDate(attri)) {
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

			computer = serv.getOneComputer(id);
			
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
					computer.setConstructor(serv.getOneCompany(company_id));
				}
			} else {
				invalidCommand(this);
				return;
			}
			
			serv.saveOneComputer(computer);
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
						if (ValidatorDate.INSTANCE.validateDate(attri)) {
							introductionDate = LocalDateTime.parse(attri+"T00:00:00", DateTimeFormatter.ISO_DATE_TIME);
						} else {
							invalidCommand(this);
						}
					} else if ( strg.startsWith("discontinuedDate") ) {
						attri = strg.substring(strg.indexOf('=')+1);
						if (ValidatorDate.INSTANCE.validateDate(attri)) {
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
				computer.setConstructor(serv.getOneCompany(company_id));
			}
			serv.saveOneComputer(computer);
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
	DELETE ("delete") {
		@Override
		public void execute(Scanner s) {
			if (s.hasNext()) {
				long id = s.nextLong();
				serv.deleteOneComputer(id);
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

	private static IService serv = new ServiceImpl();
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
	public static void printComputer(Computer c) {
		StringBuilder strBuild = new StringBuilder();
		strBuild.append(c.getId());
		strBuild.append("\t");
		strBuild.append(c.getName());
		strBuild.append("\t");
		if (c.getConstructor()!= null) {
			strBuild.append(c.getConstructor().getName());
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
	public static void printCompany(Company c) {
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
		list = (ArrayList<Computer>) serv.getAllComputer();
		for (Computer computer : list) {
			printComputer(computer);
		}
	}

	/**
	 * Display the list of companies within the Database
	 */
	private static void listCompanies() {
		ArrayList<Company> list = new ArrayList<Company>();
		list = (ArrayList<Company>) serv.getAllCompany();
		for (Company Company : list) {
			printCompany(Company);
		}
	}

	/**
	 * Display page by page the list of computers within the Database,
	 * 
	 */
	private static void listComputersPage(Scanner s) {
		Page<Computer> page;
		if (s.hasNext()) {
			Long nbLine = s.nextLong();
			page = new ComputerPage (serv, nbLine);
		} else {
			page = new ComputerPage (serv);
		}
	    BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
		Scanner scan = null;
		while(page.nextPage()) {
			for (Computer computer  : page.getElements()) {
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
		}
		if (scan != null) {
			scan.close();
		}
	}
	
	/**
	 * Display page by page the list of companies within the Database,
	 * @param s Current Scanner value
	 */
	private static void listCompaniesPage(Scanner s) {
		Page<Company> page;
		if (s.hasNext()) {
			Long nbLine = s.nextLong();
			page = new CompanyPage (serv, nbLine);
		} else {
			page = new CompanyPage (serv);
		}
	    BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
		Scanner scan = null;
		while(page.nextPage()) {
			for (Company company  : page.getElements()) {
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
		}
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
	private static void printEndOfPage (Page<?> page) {
		StringBuilder stgBuild = new StringBuilder();
		stgBuild.append("Page ");
		stgBuild.append(page.getCurrPage());
		stgBuild.append("/");
		stgBuild.append(page.getSize());
		stgBuild.append("\t (enter '");
		stgBuild.append(CliCommands.quitPages);
		stgBuild.append("' to quit)");
		System.out.println(stgBuild.toString());
	}
}
