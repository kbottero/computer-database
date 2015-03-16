package com.excilys.cdb.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Scanner;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.service.IService;
import com.excilys.cdb.service.ServiceImpl;

public class CLIApplication {
	
	
	//TODO: Replace with enum
	private final static String list = "list";
	private final static String show = "show";
	private final static String computers = "computers";
	private final static String companies = "companies";
	private final static String details = "details";
	private final static String update = "update";
	private final static String delete = "delete";
	private final static String create = "create";
	private final static String quit = "quit";
	private final static String help = "help";
	private final static String pages = "pages";
	private final static String invalidCommand = "Invalid command";
	private static IService serv = new ServiceImpl();
	
	public static void main(String args[]){
		
		String rep = new String();
		
		do{
			System.out.print(">");
			try{
			    BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
			    rep = bufferRead.readLine();
		 
			}
			catch(IOException e)
			{
				e.printStackTrace();
				break;
			}
			
			Scanner s = new Scanner(rep);
			try {
				String keyWord = s.next();
			
				switch(keyWord) {
				case list:
					list(s);
					break;
				case show:
					showComputer(s);
					break;
				case create:
					createComputer(s);
					break;
				case update:
					updateComputer(s);
					break;
				case delete:	
					deleteComputer(s);
					break;
				case help:	
					help();
					break;
				case quit:
					break;
				default:
					System.out.println(invalidCommand);
					break;
				}
			} catch (NoSuchElementException e) {
				continue;
			}
		}  while (!rep.equals(quit));
	}

	private static void list(Scanner s) {
		String keyWord = s.next();
		if (keyWord.equals(computers)) { 
			try {
				keyWord = s.next();
				if (keyWord.equals(pages)) {
					listComputersPage(s);
				} else {
					System.out.println(invalidCommand);
				}
			} catch (NoSuchElementException e) {
				listComputers();
			}
		} else if (keyWord.equals(companies)) { 
			try {
				keyWord = s.next();
				if (keyWord.equals(pages)) {
					listCompaniesPage(s);
				} else {
					System.out.println(invalidCommand);
				}
			} catch (NoSuchElementException e) {
				listCompanies();
			}
		}
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
		try {
			int nbLine = s.nextInt();
			page = new Page<Computer> ((ArrayList<Computer>)serv.getAllComputer(), nbLine);
		} catch (NoSuchElementException e) {
			page = new Page<Computer> ((ArrayList<Computer>)serv.getAllComputer());
		}
	    BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
		Scanner scan = null;
		while(page.nextPage()) {
			System.out.print(">");
			try{
				scan = new Scanner(bufferRead.readLine());
				try {
					String keyWord = scan.next();
					if(keyWord.equals("q")) {
						break;
					}
				} catch (NoSuchElementException e) {
					
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
		try {
			int nbLine = s.nextInt();
			page = new Page<Company> ((ArrayList<Company>)serv.getAllCompany(), nbLine);
		} catch (NoSuchElementException e) {
			page = new Page<Company> ((ArrayList<Company>)serv.getAllCompany());
		}
	    BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
		Scanner scan = null;
		while(page.nextPage()) {
			System.out.print(">");
			try{
				scan = new Scanner(bufferRead.readLine());
				try {
					String keyWord = scan.next();
					if(keyWord.equals("q")) {
						break;
					}
				} catch (NoSuchElementException e) {
					
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
	 * Command to display details concerning one computer
	 * @param s Current Scanner value
	 */
	private static void showComputer(Scanner s) {
		if (s.next().equals(details)) {
			try {
				long id = s.nextLong();
				Computer computer = serv.getOneComputer(id);
				if (computer != null) {
					printComputer(computer);
				} else {
					System.out.println("This id does not refer to any computer");
				}
			} catch (NoSuchElementException e) {
				System.out.println(invalidCommand);
			}
		}
	}
	
	
	/**
	 * Command to create a new computer 
	 * @param s  Current Scanner value
	 */
	private static void createComputer(Scanner s) {
		Computer computer;
		String name;
		Date introductionDate = null;
		Date discontinuedDate = null;
		try {
			name = s.next();
		} catch (NoSuchElementException e) {
			System.out.println(invalidCommand);
			return;
		}
		
		computer = new Computer(-1,name);
		
		try {
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			introductionDate = dateFormat.parse(s.next());
			discontinuedDate = dateFormat.parse(s.next());
		} catch (NoSuchElementException e) {

		} catch (ParseException e) {
			System.out.println("Invalid Date : Format (yyyy-MM-dd).");
			return;
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
	
	
	/**
	 * Command to update an existing computer 
	 * @param s  Current Scanner value
	 */
	private static void updateComputer(Scanner s) {
		Computer computer;
		long id;
		String name = null;
		Date introductionDate = null;
		Date discontinuedDate = null;
		
		try {
			id = s.nextLong();
		} catch (NoSuchElementException e) {
			System.out.println(invalidCommand);
			return;
		}
		
		try {
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			name = s.next();
			introductionDate = dateFormat.parse(s.next());
			discontinuedDate = dateFormat.parse(s.next());
			
		} catch (NoSuchElementException e) {

		} catch (ParseException e) {
			System.out.println("Invalid Date : Format (yyyy-MM-dd).");
			return;
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
		} else {
			return;
		}
		
		serv.saveOneComputer(computer);
	}
	
	/**
	 * Command to delete an existing computer 
	 * @param s  Current Scanner value
	 */
	private static void deleteComputer(Scanner s) {
		try {
			long id = s.nextLong();
			serv.deleteOneComputer(id);
		} catch (NoSuchElementException e) {
			System.out.println(invalidCommand);
		}
	}
	
	/**
	 * Command to display existing commands 
	 */
	private static void help() {
		System.out.println("Help : list commands");
		System.out.println(list+" "+computers+" [pages [nbLinePerPages]]");
		System.out.println(list+" "+companies+" [pages [nbLinePerPages]]");
		System.out.println(show+" id");
		System.out.println(create+" name [introductionDate [discontinuedDate]]");
		System.out.println(update+" id [name [introductionDate [discontinuedDate]]]");
		System.out.println(delete+" id");
		System.out.println(quit);
	}
	
	/**
	 * Display a computer
	 */
	public static void printComputer(Computer c) {
		System.out.println(c.getId()+"\t"+c.getName()+"\t"+c.getConstructor().getName()+"\t"+c.getIntroductionDate()+"\t"+c.getDiscontinuedDate());
	}
	
	/**
	 * Display a company
	 */
	public static void printCompany(Company c) {
		System.out.println(c.getId()+"\t"+c.getName());
	}
	
}
