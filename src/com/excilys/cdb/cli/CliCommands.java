package com.excilys.cdb.cli;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.service.IService;
import com.excilys.cdb.service.ServiceImpl;
import com.excilys.cdb.validation.ValidatorDate;

public enum CliCommands {
	
	LIST_COMPUTERS ("list_computers") {
		@Override
		public void execute(Scanner s) {
			String keyWord;
			if (s.hasNext()) {
				keyWord = s.next();
				if (keyWord.equals(pages)) {
					listComputersPage(s);
				} else {
					System.out.print(invalidCommand);
					System.out.print(" : ");
					getHelp();
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
	LIST_COMPANIES ("list_companies") {
		@Override
		public void execute(Scanner s) {
			String keyWord;
			if (s.hasNext()) {
				keyWord = s.next();
				if (keyWord.equals(pages)) {
					listCompaniesPage(s);
				} else {
					System.out.print(invalidCommand);System.out.print(invalidCommand);
					System.out.print(" : ");
					getHelp();
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
				System.out.print(invalidCommand);
				System.out.print(" : ");
				getHelp();
			}
			
		}
		@Override
		public String getHelp() {
			return  this.getLabel()+" id";
		}
	},
	UPDATE ("update") {
		@Override
		public void execute(Scanner s) {
			Computer computer;
			long id;
			String name = null;
			LocalDateTime introductionDate = null;
			LocalDateTime discontinuedDate = null;
			
			if (s.hasNext()) {
				id = s.nextLong();
			} else {
				System.out.print(invalidCommand);
				System.out.print(" : ");
				getHelp();
				return;
			}
			
			name = s.next();
			introductionDate = LocalDateTime.parse(s.next(), DateTimeFormatter.ISO_LOCAL_DATE);
			discontinuedDate = LocalDateTime.parse(s.next(), DateTimeFormatter.ISO_LOCAL_DATE);
			
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
			} else {
				System.out.print(invalidCommand);
				System.out.print(" : ");
				getHelp();
				return;
			}
			
			serv.saveOneComputer(computer);
		}
		@Override
		public String getHelp() {
			return  this.getLabel()+" id [name [introductionDate [discontinuedDate]]]";
		}
	},
	CREATE ("create") {
		@Override
		public void execute(Scanner s) {
			Computer computer;
			String name;
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
				String date = s.next();
				if (ValidatorDate.INSTANCE.validateDate(date)) {
					introductionDate = LocalDateTime.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);
				}
			}
			if (s.hasNext()) {
				String date = s.next();
				if (ValidatorDate.INSTANCE.validateDate(date)) {
					discontinuedDate = LocalDateTime.parse(s.next(), DateTimeFormatter.ISO_LOCAL_DATE);
				}
			}
			
			if (introductionDate != null) {
				computer.setIntroductionDate(introductionDate);
			}
			if (discontinuedDate != null) {
				computer.setDiscontinuedDate(discontinuedDate);
			}
			serv.saveOneComputer(computer);
			if (computer.getId() != -1) {
				System.out.println("Computer created. Id = "+computer.getId());
			}
		}
		@Override
		public String getHelp() {
			return this.getLabel()+" name [introductionDate [discontinuedDate]]";
		}
	},
	DELETE ("delete") {
		@Override
		public void execute(Scanner s) {
			if (s.hasNext()) {
				long id = s.nextLong();
				serv.deleteOneComputer(id);
			} else {
				System.out.println(invalidCommand);
			}
		}
		@Override
		public String getHelp() {
			return this.getLabel()+" id";
		}
	},
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
				for (CliCommands cliCom : CliCommands.values()) {
					if(comm.equals(cliCom.getLabel())) {
						return cliCom;
					}
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
			int nbLine = s.nextInt();
			page = new Page<Computer> ((ArrayList<Computer>)serv.getAllComputer(), nbLine);
		} else {
			page = new Page<Computer> ((ArrayList<Computer>)serv.getAllComputer());
		}
	    BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
		Scanner scan = null;
		while(page.nextPage()) {
			System.out.print(">");
			try{
				scan = new Scanner(bufferRead.readLine());
				if (scan.hasNext()) {
					String keyWord = scan.next();
					if(keyWord.equals("q")) {
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
			int nbLine = s.nextInt();
			page = new Page<Company> ((ArrayList<Company>)serv.getAllCompany(), nbLine);
		} else {
			page = new Page<Company> ((ArrayList<Company>)serv.getAllCompany());
		}
	    BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
		Scanner scan = null;
		while(page.nextPage()) {
			System.out.print(">");
			try{
				scan = new Scanner(bufferRead.readLine());
				if (scan.hasNext()) {
					String keyWord = scan.next();
					if(keyWord.equals("q")) {
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
	
}
