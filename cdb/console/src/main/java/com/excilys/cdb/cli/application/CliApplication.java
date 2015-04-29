package com.excilys.cdb.cli.application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.excilys.cdb.cli.CliCommands;
import com.excilys.cdb.cli.CliCommands.Commands;

@Component
public class CliApplication {
	
	@Autowired
	private CliCommands cliCommands;
	
	public static void main(String args[]){
		
		@SuppressWarnings("resource")
		ApplicationContext context = 
	            new ClassPathXmlApplicationContext("classpath:application-context.xml");

		CliApplication cliApplication = context.getBean(CliApplication.class);
		
		Logger logger = LoggerFactory.getLogger(CliApplication.class);
		logger.info("Beginning of the CLI application");
		String rep = new String();
		do {
			System.out.print(CliCommands.prompt);
			try {
			    BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
			    rep = bufferRead.readLine();
			} catch(IOException e) {
				e.printStackTrace();
				break;
			}
			Scanner s = new Scanner(rep);
			cliApplication.cliCommands.getCommands(s);
		} while (!rep.equals(Commands.QUIT.getLabel()));
		logger.info("End of the CLI application");
	}
}
